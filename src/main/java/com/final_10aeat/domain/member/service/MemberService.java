package com.final_10aeat.domain.member.service;

import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberWithdrawRequestDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.MemberDuplicatedException;
import com.final_10aeat.domain.member.exception.MemberMissMatchException;
import com.final_10aeat.domain.member.exception.MemberNotExistException;
import com.final_10aeat.domain.member.repository.BuildingInfoRepository;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;
    private final BuildingInfoRepository buildingInfoRepository;

    public void register(MemberRegisterRequestDto request) {

        if (memberRepository.existsByEmailAndDeletedAtIsNull(request.email())) {
            throw new MemberDuplicatedException();
        }

        String password = passwordEncoder.encode(request.password());

        BuildingInfo buildingInfo = BuildingInfo.builder()
                .dong(request.dong())
                .ho(request.ho())
                .office(null)
                .build();

        BuildingInfo savedBuildingInfo = buildingInfoRepository.save(buildingInfo);

        Member member = Member.builder()
                .email(request.email())
                .password(password)
                .name(request.name())
                .role(request.memberRole())
                .buildingInfos(Set.of(savedBuildingInfo))
                .build();

        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public String login(MemberLoginRequestDto request) {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(request.email())
                .orElseThrow(MemberNotExistException::new);

        if (!passwordMatcher(request.password(), member)) {
            throw new MemberNotExistException();
        }

        return jwtTokenGenerator.createJwtToken(request.email(),member.getRole());
    }

    public void withdraw(MemberWithdrawRequestDto request) {
        Member member = memberRepository.findByEmailAndDeletedAtIsNull(request.email())
                .orElseThrow(MemberNotExistException::new);

        if (!passwordMatcher(request.password(), member)) {
            throw new MemberMissMatchException();
        }

        member.delete(LocalDateTime.now());
    }

    private Boolean passwordMatcher(final String requestPassword, final Member member) {
        return passwordEncoder.matches(requestPassword, member.getPassword());
    }
}
