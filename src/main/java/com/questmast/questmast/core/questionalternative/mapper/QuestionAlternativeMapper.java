package com.questmast.questmast.core.questionalternative.mapper;

import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeDTO;

public class QuestionAlternativeMapper {
    public static QuestionAlternativeDTO convertFromEntityToDTO(QuestionAlternative questionAlternative) {
        return new QuestionAlternativeDTO(questionAlternative.getId(), questionAlternative.getAlternativeLetter(), questionAlternative.getStatement(), questionAlternative.getIsCorrect());
    }
}