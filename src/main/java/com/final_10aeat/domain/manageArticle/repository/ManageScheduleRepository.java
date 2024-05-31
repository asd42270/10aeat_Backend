package com.final_10aeat.domain.manageArticle.repository;

import com.final_10aeat.domain.manageArticle.entity.ManageSchedule;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ManageScheduleRepository extends JpaRepository<ManageSchedule, Long> {

    @Lock(LockModeType.PESSIMISTIC_READ)
    @Query("SELECT s FROM ManageSchedule s WHERE s.id = :id")
    Optional<ManageSchedule> findAndPessimisticLockById(Long id);
}
