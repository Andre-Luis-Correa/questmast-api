package com.questmast.questmast.core.questionalternative.repository;

import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAlternativeRepository extends JpaRepository<QuestionAlternative, Long> {
}