package com.final_10aeat.global.security.principal;

import com.final_10aeat.domain.member.exception.UserException;
import com.final_10aeat.domain.member.repository.MemberRepository;
import com.final_10aeat.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
@Component //Service보다는 Component의 성격에 가까움.
@RequiredArgsConstructor
//TODO: 클래스 이름 변경 고려
public class MemberPrincipalService {

    private final MemberRepository memberRepository;



    public UserDetails loadUserByUsername(String email){
        return new MemberPrincipal(memberRepository.loadUserByUsername(email));
    }
}
