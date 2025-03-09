package com.questmast.questmast.core.questionnaire.domain.dto;

import com.questmast.questmast.core.question.domain.dto.QuestionFilterDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record QuestionnaireFormDTO(

        @NotBlank
        String name,

        @NotBlank
        String studentEmail,

        @NotNull
        Boolean isPublic,

        @Valid
        @NotNull
        List<QuestionnaireQuestionFormDTO> questionnaireQuestionFormDTOList,

        QuestionFilterDTO questionFilterDTO

) {
}
