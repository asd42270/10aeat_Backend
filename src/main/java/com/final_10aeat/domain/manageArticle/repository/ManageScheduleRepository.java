package com.final_10aeat.domain.manageArticle.repository;

import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ManageScheduleRepository extends JpaRepository<ManageSchedule, Long> {

    // TODO 리팩토링 기간 중 변경 예정
//        @Query("""
//            UPDATE ManageSchedule s
//            SET s.complete = NOT s.complete
//            WHERE s.id = :id
//
//            """)
//    @Query("SELECT s FROM ManageSchedule s WHERE s.id = :id")
//    Optional<ManageSchedule> findAndPessimisticLockById(Long id);
}
