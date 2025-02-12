package com.questmast.questmast.core.professionallevel.repository;

import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProfessionalLevelRepository extends JpaRepository<ProfessionalLevel, Long> {
}