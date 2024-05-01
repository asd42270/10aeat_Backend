package com.final_10aeat.domain.member.repository;


import com.final_10aeat.domain.member.entity.Member;
import com.final_10aeat.domain.member.exception.MemberNotExistException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);

    default Member loadUserByUsername(String email){
        return findByEmail(email).orElseThrow(MemberNotExistException::new);
    }
}
