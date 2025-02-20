package com.questmast.questmast.core.contact.email.repository;

import com.questmast.questmast.core.contact.email.domain.entity.Email;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<Email, String> {
    boolean existsByEmail(String email);
}
