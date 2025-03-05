package com.questmast.questmast.core.question.domain.dto;

import com.questmast.questmast.core.subject.domain.dto.SubjectFilterDTO;

import java.util.List;

public record QuestionFilterDTO(
        List<Long> boardExaminerIds,
        List<Long> institutionIds,
        List<Long> functionIds,
        List<Long> questionDifficultyLevelIds,
        List<SubjectFilterDTO> subjectFilterDTOList
) {
}
