package com.final_10aeat.domain.member.repository;

import com.final_10aeat.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByEmailAndDeletedAtIsNull(String email);

    Optional<Member> findByEmailAndDeletedAtIsNull(String email);
}
