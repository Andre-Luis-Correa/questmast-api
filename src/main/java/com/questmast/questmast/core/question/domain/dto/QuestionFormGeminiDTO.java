package com.questmast.questmast.core.question.domain.dto;

import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeFormDTO;
import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeFormGeminiDTO;

import java.util.List;

public record QuestionFormGeminiDTO(

        String name,

        String statement,

        String explanation,

        List<QuestionAlternativeFormGeminiDTO> questionAlternativeList

        ) {
}
