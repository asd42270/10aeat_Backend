package com.final_10aeat.domain.member.service;

import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.dto.request.MemberRegisterRequestDto;
import com.final_10aeat.domain.member.dto.request.MemberRequestDto;
import com.final_10aeat.domain.member.exception.MemberDuplicatedException;
import com.final_10aeat.domain.member.exception.MemberNotExistException;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.global.security.jwt.JwtTokenGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenGenerator jwtTokenGenerator;

    //TODO: 응답 값에 대한 부분은 프론트와 논의 필요
    public void register(MemberRegisterRequestDto request) {
        //1. 유효성 검사
        if (memberRepository.existsByEmail(request.email())) {
            throw new MemberDuplicatedException();
        }

        //2. 비밀번호 인코딩
        String password = passwordEncoder.encode(request.password());

        //3. member로 변환
        Member member = Member.builder()
                .email(request.email())
                .password(password)
                .build();

        //4. save
        memberRepository.save(member);
    }

    public String login(MemberRequestDto request) {
        Member member = memberRepository.loadUserByUsername(request.email());

        if (!passwordMatcher(request.password(), member)) {
            throw new MemberNotExistException();
        }

        return jwtTokenGenerator.createJwtToken(request.email());
    }

    private Boolean passwordMatcher(final String requestPassword, final Member member) {
        return passwordEncoder.matches(requestPassword, member.getPassword());
    }


}
