package com.questmast.questmast.core.questiondifficultylevel.mapper;

import com.questmast.questmast.core.questiondifficultylevel.domain.dto.QuestionDifficultyLevelDTO;
import com.questmast.questmast.core.questiondifficultylevel.domain.dto.QuestionDifficultyLevelFormDTO;
import com.questmast.questmast.core.questiondifficultylevel.domain.entity.QuestionDifficultyLevel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionDifficultyLevelMapStructMapper {
    QuestionDifficultyLevel convertQuestionDifficultyLevelFormDTOToQuestionDifficultyLevel(QuestionDifficultyLevelFormDTO questionDifficultyLevelFormDTO);

    QuestionDifficultyLevelDTO convertQuestionDifficultyLevelToQuestionDifficultyLevelDetailsDTO(QuestionDifficultyLevel questionDifficultyLevel);
}