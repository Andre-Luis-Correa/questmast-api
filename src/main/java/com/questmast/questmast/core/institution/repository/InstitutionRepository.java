package com.questmast.questmast.core.institution.repository;

import com.questmast.questmast.core.institution.domain.model.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long> {
}
