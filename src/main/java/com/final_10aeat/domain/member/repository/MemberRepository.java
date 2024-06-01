package com.final_10aeat.domain.member.repository;

import com.final_10aeat.domain.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmailAndDeletedAtIsNull(String email);

    Optional<Member> findByEmailAndDeletedAtIsNull(String email);

    @EntityGraph(attributePaths = {"buildingInfos"})
    @Query("SELECT m FROM Member m JOIN FETCH m.buildingInfos WHERE m.id = :id")
    Optional<Member> findMemberByIdWithBuildingInfos(@Param("id") Long id);
}
