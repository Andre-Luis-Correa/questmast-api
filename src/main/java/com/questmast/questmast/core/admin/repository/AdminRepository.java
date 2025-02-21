package com.questmast.questmast.core.admin.repository;

import com.questmast.questmast.core.admin.domain.model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByMainEmail(String email);
}
