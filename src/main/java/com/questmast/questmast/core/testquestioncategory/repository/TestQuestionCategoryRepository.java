package com.questmast.questmast.core.testquestioncategory.repository;

import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuestionCategoryRepository extends JpaRepository<TestQuestionCategory, Long> {
}