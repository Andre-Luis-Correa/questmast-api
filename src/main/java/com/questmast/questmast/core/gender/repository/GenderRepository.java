package com.questmast.questmast.core.gender.repository;

import com.questmast.questmast.core.gender.domain.Gender;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenderRepository extends JpaRepository<Gender, String> {
    Optional<Gender> findByAcronym(String acronym);
}
