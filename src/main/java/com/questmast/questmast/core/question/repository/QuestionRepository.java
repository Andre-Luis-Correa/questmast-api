package com.questmast.questmast.core.question.repository;

import com.questmast.questmast.core.question.domain.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    void deleteById(Long id);

    List<Question> findByIsGeneratedByAiFalse();

}
