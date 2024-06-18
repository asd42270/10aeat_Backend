package com.final_10aeat.domain.member.service;

import com.final_10aeat.domain.manager.exception.ExpiredVerificationCodeException;
import com.final_10aeat.common.dto.LoginRequestDto;
import com.final_10aeat.domain.member.dto.request.CreateMemberRequestDto;
import com.final_10aeat.domain.member.dto.request.DeleteMemberRequestDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.DisagreementException;
import com.final_10aeat.domain.member.exception.EmailDuplicateException;
import com.final_10aeat.domain.member.exception.PasswordMissMatchException;
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

    public LoginRequestDto register(CreateMemberRequestDto request) {
        validateEmail(request.email());
        ensureTermsAgreed(request.termAgreed());

        Long officeId = getOfficeIdFromRedis(request.email());
        validateDongHo(request);

        BuildingInfo savedBuildingInfo = saveBuildingInfo(request, officeId);
        saveMember(request, officeId, savedBuildingInfo);

        return new LoginRequestDto(request.email(), request.password());
    }

    private void validateEmail(String email) {
        if (memberRepository.existsByEmailAndDeletedAtIsNull(email)) {
            throw new EmailDuplicateException();
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
            throw new ExpiredVerificationCodeException();
        }

        JsonObject userInfo = new Gson().fromJson(userInfoJson, JsonObject.class);
        return userInfo.get("officeId").getAsLong();
    }

    private void validateDongHo(CreateMemberRequestDto request) {
        JsonObject userInfo = fetchUserInfoFromRedis(request.email());
        if (!userInfo.get("dong").getAsString().equals(request.dong()) ||
            !userInfo.get("ho").getAsString().equals(request.ho())) {
            throw new PasswordMissMatchException("관리자가 입력한 동, 호수와 일치하지 않습니다.");
        }
    }

    private BuildingInfo saveBuildingInfo(CreateMemberRequestDto request, Long officeId) {
        BuildingInfo buildingInfo = BuildingInfo.builder()
            .dong(request.dong())
            .ho(request.ho())
            .office(new Office(officeId))
            .build();

        return buildingInfoRepository.save(buildingInfo);
    }

    private void saveMember(CreateMemberRequestDto request, Long officeId,
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
    public String login(LoginRequestDto request) {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(request.email())
            .orElseThrow(UserNotExistException::new);

        if (!passwordMatcher(request.password(), member)) {
            throw new PasswordMissMatchException();
        }

        return jwtTokenGenerator.createJwtToken(request.email(), member.getRole());
    }

    public void withdraw(DeleteMemberRequestDto request) {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(request.email())
            .orElseThrow(UserNotExistException::new);

        if (!passwordMatcher(request.password(), member)) {
            throw new PasswordMissMatchException();
        }

        member.delete(LocalDateTime.now());
    }

    private Boolean passwordMatcher(final String requestPassword, final Member member) {
        return passwordEncoder.matches(requestPassword, member.getPassword());
    }
}
