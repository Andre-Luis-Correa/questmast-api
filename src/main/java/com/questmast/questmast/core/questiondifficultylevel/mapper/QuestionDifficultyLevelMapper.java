package com.questmast.questmast.core.questiondifficultylevel.mapper;

import com.questmast.questmast.core.questiondifficultylevel.domain.dto.QuestionDifficultyLevelDTO;
import com.questmast.questmast.core.questiondifficultylevel.domain.entity.QuestionDifficultyLevel;

public class QuestionDifficultyLevelMapper {
    public static QuestionDifficultyLevelDTO convertFromEntityToDTO(QuestionDifficultyLevel questionDifficultyLevel) {
        return new QuestionDifficultyLevelDTO(questionDifficultyLevel.getId(), questionDifficultyLevel.getName(), questionDifficultyLevel.getDescription());
    }
}