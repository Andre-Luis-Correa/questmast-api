package com.questmast.questmast.core.testquestioncategory.mapper;

import com.questmast.questmast.core.testquestioncategory.domain.dto.TestQuestionCategoryDTO;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;

public class TestQuestionCategoryMapper {
    public static TestQuestionCategoryDTO convertFromEntityToDTO(TestQuestionCategory testQuestionCategory) {
        return new TestQuestionCategoryDTO(testQuestionCategory.getId(), testQuestionCategory.getName());
    }
}
