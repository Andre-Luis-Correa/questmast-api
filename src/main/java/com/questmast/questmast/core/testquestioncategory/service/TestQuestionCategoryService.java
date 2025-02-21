package com.questmast.questmast.core.testquestioncategory.service;

//import com.questmast.questmast.exceptions.type.EntityNotFoundException;
import com.questmast.questmast.core.testquestioncategory.domain.dto.TestQuestionCategoryDTO;
import com.questmast.questmast.core.testquestioncategory.domain.dto.TestQuestionCategoryFormDTO;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import com.questmast.questmast.core.testquestioncategory.mapper.TestQuestionCategoryMapStructMapper;
import com.questmast.questmast.core.testquestioncategory.mapper.TestQuestionCategoryMapper;
import com.questmast.questmast.core.testquestioncategory.repository.TestQuestionCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestQuestionCategoryService {

    private final TestQuestionCategoryRepository testQuestionCategoryRepository;
    private final TestQuestionCategoryMapStructMapper testQuestionCategoryMapper;

    public TestQuestionCategory findById(Long id) {
        return testQuestionCategoryRepository.findById(id).orElseThrow(
                //() -> new EntityNotFoundException("TestQuestionCategory", "id", id.toString())
        );
    }

    public TestQuestionCategory save(TestQuestionCategoryFormDTO testQuestionCategoryFormDTO) {
        TestQuestionCategory testQuestionCategory = convertTestQuestionCategoryFormDTOToTestQuestionCategory(testQuestionCategoryFormDTO);
        return testQuestionCategoryRepository.save(testQuestionCategory);
    }

    private TestQuestionCategory convertTestQuestionCategoryFormDTOToTestQuestionCategory(TestQuestionCategoryFormDTO testQuestionCategoryFormDTO) {
        return testQuestionCategoryMapper.convertTestQuestionCategoryFormDTOToTestQuestionCategory(testQuestionCategoryFormDTO);
    }

    public TestQuestionCategoryDTO convertTestQuestionCategoryToTestQuestionCategoryDTO(TestQuestionCategory testQuestionCategory) {
        return testQuestionCategoryMapper.convertTestQuestionCategoryToTestQuestionCategoryDTO(testQuestionCategory);
    }

    public Page<TestQuestionCategory> findAll(Pageable pageable) {
        return testQuestionCategoryRepository.findAll(pageable);
    }

    public Page<TestQuestionCategoryDTO> convertToTestQuestionCategoryDTOPage(Page<TestQuestionCategory> testQuestionCategoryPage) {
        return testQuestionCategoryPage.map(testQuestionCategoryMapper::convertTestQuestionCategoryToTestQuestionCategoryDTO);
    }

    public void delete(TestQuestionCategory testQuestionCategory) {
        testQuestionCategoryRepository.delete(testQuestionCategory);
    }

    public List<TestQuestionCategory> findAll() {
        return testQuestionCategoryRepository.findAll();
    }

    public List<TestQuestionCategoryDTO> convertToTestQuestionCategoryDTOList(List<TestQuestionCategory> testQuestionCategoryList) {
        return testQuestionCategoryList.stream().map(testQuestionCategoryMapper::convertTestQuestionCategoryToTestQuestionCategoryDTO).collect(Collectors.toList());
    }

    public TestQuestionCategoryDTO update(TestQuestionCategory testQuestionCategory, TestQuestionCategoryFormDTO testQuestionCategoryFormDTO) {
        TestQuestionCategory updatedTestQuestionCategory = convertTestQuestionCategoryFormDTOToTestQuestionCategory(testQuestionCategoryFormDTO);
        updatedTestQuestionCategory.setId(testQuestionCategory.getId());

        testQuestionCategoryRepository.save(updatedTestQuestionCategory);
        return convertTestQuestionCategoryToTestQuestionCategoryDTO(updatedTestQuestionCategory);
    }
}