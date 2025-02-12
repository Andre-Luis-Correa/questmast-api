package com.questmast.questmast.core.testquestioncategory.mapper;

import com.questmast.questmast.core.testquestioncategory.domain.dto.TestQuestionCategoryDTO;
import com.questmast.questmast.core.testquestioncategory.domain.dto.TestQuestionCategoryFormDTO;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TestQuestionCategoryMapStructMapper {

    TestQuestionCategory convertTestQuestionCategoryFormDTOToTestQuestionCategory(TestQuestionCategoryFormDTO testQuestionCategoryFormDTO);

    TestQuestionCategoryDTO convertTestQuestionCategoryToTestQuestionCategoryDTO(TestQuestionCategory testQuestionCategory);
}
