package com.questmast.questmast.core.questionalternative.mapper;

import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeDTO;
import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeFormDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface QuestionAlternativeMapStructMapper {
    QuestionAlternative convertQuestionAlternativeFormDTOToQuestionAlternative(QuestionAlternativeFormDTO questionAlternativeFormDTO);

    QuestionAlternativeDTO convertQuestionAlternativeToQuestionAlternativeDetailsDTO(QuestionAlternative questionAlternative);
}