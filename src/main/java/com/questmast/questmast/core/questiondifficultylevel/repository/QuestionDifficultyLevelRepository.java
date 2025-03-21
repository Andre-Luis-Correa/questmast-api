package com.questmast.questmast.core.questiondifficultylevel.repository;

import com.questmast.questmast.core.questiondifficultylevel.domain.entity.QuestionDifficultyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionDifficultyLevelRepository extends JpaRepository<QuestionDifficultyLevel, Long> {
    List<QuestionDifficultyLevel> findAllByIdIn(List<Long> ids);
}