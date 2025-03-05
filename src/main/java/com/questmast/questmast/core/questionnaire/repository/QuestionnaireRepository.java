package com.questmast.questmast.core.questionnaire.repository;

import com.questmast.questmast.core.questionnaire.domain.model.Questionnaire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long> {
}
