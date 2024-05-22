package com.final_10aeat.domain.manager.repository;

import com.final_10aeat.domain.manager.entity.Manager;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByEmail(String email);
}
