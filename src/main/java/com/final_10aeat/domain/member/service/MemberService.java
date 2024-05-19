package com.final_10aeat.domain.member.service;

import com.final_10aeat.domain.member.dto.request.MemberLoginRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberWithdrawRequestDto;
import com.final_10aeat.domain.member.entity.BuildingInfo;
import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.MemberDuplicatedException;
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

    //TODO: 응답 값에 대한 부분은 프론트와 논의 필요
    public void register(MemberRegisterRequestDto request) {
        // 1. 유효성 검사
        if (memberRepository.existsByEmail(request.email())) {
            throw new MemberDuplicatedException();
        }

        // 2. 비밀번호 인코딩
        String password = passwordEncoder.encode(request.password());

        // 3. dong, ho를 BuildingInfo로 변환, member로 변환
        BuildingInfo buildingInfo = BuildingInfo.builder()
                .dong(request.dong())
                .ho(request.ho())
                .office(null)
                .build();

        //BuildingInfo를 먼저 저장해준다.
        BuildingInfo savedBuildingInfo = buildingInfoRepository.save(buildingInfo);

        Member member = Member.builder()
                .email(request.email())
                .password(password)
                .role(request.memberRole())
                .buildingInfos(Set.of(savedBuildingInfo))
                .build();

        // 4. save
        memberRepository.save(member);
    }

    @Transactional(readOnly = true)
    public String login(MemberLoginRequestDto request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(MemberNotExistException::new);

        if (!passwordMatcher(request.password(), member)) {
            throw new MemberNotExistException();
        }

        return jwtTokenGenerator.createJwtToken(request.email(),member.getRole());
    }

    public void withdraw(MemberWithdrawRequestDto request) {
        Member member = memberRepository.findByEmail(request.email())
                .orElseThrow(MemberNotExistException::new);

        if (!passwordMatcher(request.password(), member)) {
            throw new MemberNotExistException();
        }

        member.delete(LocalDateTime.now());
    }

    private Boolean passwordMatcher(final String requestPassword, final Member member) {
        return passwordEncoder.matches(requestPassword, member.getPassword());
    }
}
