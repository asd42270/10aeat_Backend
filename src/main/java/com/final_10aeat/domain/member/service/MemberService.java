package com.final_10aeat.domain.member.service;

import com.final_10aeat.domain.manager.exception.VerificationCodeExpiredException;
import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberWithdrawRequestDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.DisagreementException;
import com.final_10aeat.domain.member.exception.EmailDuplicatedException;
import com.final_10aeat.domain.member.exception.MemberMissMatchException;
import com.final_10aeat.domain.member.exception.UserNotExistException;
import com.final_10aeat.domain.member.repository.BuildingInfoRepository;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.domain.office.entity.Office;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final BuildingInfoRepository buildingInfoRepository;
    private final RedisTemplate<String, String> redisTemplate;

    public MemberLoginRequestDto register(MemberRegisterRequestDto request) {
        validateEmail(request.email());
        ensureTermsAgreed(request.termAgreed());

        Long officeId = getOfficeIdFromRedis(request.email());
        validateDongHo(request);

        BuildingInfo savedBuildingInfo = saveBuildingInfo(request, officeId);
        saveMember(request, officeId, savedBuildingInfo);

        return new MemberLoginRequestDto(request.email(), request.password());
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new EmailDuplicatedException();
        }
    }

    private void ensureTermsAgreed(Boolean termAgreed) {
        if (!termAgreed) {
            throw new DisagreementException();
        }
    }

    private Long getOfficeIdFromRedis(String email) {
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        String userInfoJson = ops.get(email + ":info");
        if (userInfoJson == null) {
            throw new VerificationCodeExpiredException();
        }

        JsonObject userInfo = new Gson().fromJson(userInfoJson, JsonObject.class);
        return userInfo.get("officeId").getAsLong();
    }

    private void validateDongHo(MemberRegisterRequestDto request) {
        JsonObject userInfo = fetchUserInfoFromRedis(request.email());
        if (!userInfo.get("dong").getAsString().equals(request.dong()) ||
            !userInfo.get("ho").getAsString().equals(request.ho())) {
            throw new MemberMissMatchException("관리자가 입력한 동, 호수와 일치하지 않습니다.");
        }
    }

    private BuildingInfo saveBuildingInfo(MemberRegisterRequestDto request, Long officeId) {
        BuildingInfo buildingInfo = BuildingInfo.builder()
            .dong(request.dong())
            .ho(request.ho())
            .office(new Office(officeId))
            .build();

        return buildingInfoRepository.save(buildingInfo);
    }

    private void saveMember(MemberRegisterRequestDto request, Long officeId,
        BuildingInfo savedBuildingInfo) {
        String encodedPassword = passwordEncoder.encode(request.password());
        Member member = Member.builder()
            .email(request.email())
            .password(encodedPassword)
            .name(request.name())
            .role(request.memberRole())
            .defaultOffice(officeId)
            .buildingInfos(Set.of(savedBuildingInfo))
            .termAgreed(request.termAgreed())
            .build();

        memberRepository.save(member);
    }

    private JsonObject fetchUserInfoFromRedis(String email) {
        String userInfoJson = redisTemplate.opsForValue().get(email + ":info");
        return new Gson().fromJson(userInfoJson, JsonObject.class);
    }

    @Transactional(readOnly = true)
    public String login(MemberLoginRequestDto request) {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(request.email())
            .orElseThrow(UserNotExistException::new);

        if (!passwordMatcher(request.password(), member)) {
            throw new UserNotExistException();
        }

        return jwtTokenGenerator.createJwtToken(request.email(), member.getRole());
    }

    public void withdraw(MemberWithdrawRequestDto request) {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(request.email())
            .orElseThrow(UserNotExistException::new);

        if (!passwordMatcher(request.password(), member)) {
            throw new MemberMissMatchException();
        }

        member.delete(LocalDateTime.now());
    }

    private Boolean passwordMatcher(final String requestPassword, final Member member) {
        return passwordEncoder.matches(requestPassword, member.getPassword());
    }
}
