package com.questmast.questmast.core.solvedquestionnaire.repository;

import com.questmast.questmast.core.questionnaire.domain.model.Questionnaire;
import com.questmast.questmast.core.solvedquestionnaire.domain.model.SolvedQuestionnaire;
import com.questmast.questmast.core.student.domain.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SolvedQuestionnaireRepository extends JpaRepository<SolvedQuestionnaire, Long>, JpaSpecificationExecutor<SolvedQuestionnaire> {
    Optional<SolvedQuestionnaire> findFirstByStudentAndQuestionnaireOrderByEndDateTimeDesc(Student student, Questionnaire questionnaire);

    List<SolvedQuestionnaire> findAllByStudent(Student student);
}
