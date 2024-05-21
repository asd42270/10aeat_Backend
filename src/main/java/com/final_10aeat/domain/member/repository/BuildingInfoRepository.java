package com.final_10aeat.domain.member.repository;

import com.final_10aeat.domain.member.entity.BuildingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BuildingInfoRepository extends JpaRepository<BuildingInfo, Long> {
}
