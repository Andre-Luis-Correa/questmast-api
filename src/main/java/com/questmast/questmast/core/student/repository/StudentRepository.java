package com.questmast.questmast.core.student.repository;

import com.questmast.questmast.core.student.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByMainEmail(String email);

    Optional<Student> findByRecoveryEmail(String email);
}
