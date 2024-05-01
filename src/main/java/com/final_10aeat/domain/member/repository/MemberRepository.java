package com.final_10aeat.domain.member.repository;


import com.final_10aeat.domain.member.Member;
import com.final_10aeat.global.exception.ErrorCode;
import com.final_10aeat.domain.member.exception.UserException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    default Member loadUserByUsername(String email){
        return findByEmail(email).orElseThrow(UserException::new);
    }
}
