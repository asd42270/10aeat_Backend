package com.final_10aeat.domain.admin.repository;

import com.final_10aeat.domain.admin.entity.Admin;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {

    Optional<Admin> findByEmail(String email);
}
