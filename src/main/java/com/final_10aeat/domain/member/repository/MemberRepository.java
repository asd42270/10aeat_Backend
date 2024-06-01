package com.final_10aeat.domain.member.repository;

import com.final_10aeat.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {
    Boolean existsByEmailAndDeletedAtIsNull(String email);

    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    @Query("""
    select m
    from Member m
    join fetch m.buildingInfos b
    where m.id = :id and m.deletedAt is null
""")
    Optional<Member> findByIdWithBuildingInfos(@Param("id") Long memberId);
}
