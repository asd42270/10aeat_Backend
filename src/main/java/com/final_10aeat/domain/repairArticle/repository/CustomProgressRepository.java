package com.final_10aeat.domain.repairArticle.repository;

import com.final_10aeat.domain.repairArticle.entity.CustomProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CustomProgressRepository extends JpaRepository<CustomProgress, Long> {

    List<CustomProgress> findAllByOrderByStartScheduleAsc();
}
