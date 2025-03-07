package com.questmast.questmast.core.solvedselectionprocesstest.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.common.specification.BaseSpecification;
import com.questmast.questmast.common.specification.Search;
import com.questmast.questmast.common.specification.SpecificationUtils;
import com.questmast.questmast.core.google.service.GoogleStorageService;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.solvedevaluationtestquestion.domain.model.SolvedEvaluationTestQuestion;
import com.questmast.questmast.core.solvedevaluationtestquestion.service.SolvedEvaluationTestQuestionService;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessFilterDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessTestDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessTestFormDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.model.SolvedSelectionProcessTest;
import com.questmast.questmast.core.solvedselectionprocesstest.repository.SolvedSelectionProcessTestRepository;
import com.questmast.questmast.core.student.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolvedSelectionProcessTestService {

    private final SolvedEvaluationTestQuestionService solvedEvaluationTestQuestionService;
    private final SolvedSelectionProcessTestRepository solvedSelectionProcessTestRepository;
    private final GoogleStorageService googleStorageService;

    public SolvedSelectionProcessTest create(SelectionProcessTest selectionProcessTest, SolvedSelectionProcessTestFormDTO solvedSelectionProcessTestFormDTO, Student student, List<SolvedEvaluationTestQuestion> solvedEvaluationTestQuestionList) {
        SolvedSelectionProcessTest solvedSelectionProcessTest = new SolvedSelectionProcessTest();
        solvedSelectionProcessTest.setSelectionProcessTest(selectionProcessTest);
        solvedSelectionProcessTest.setStartDateTime(solvedSelectionProcessTestFormDTO.startDateTime());
        solvedSelectionProcessTest.setEndDateTime(solvedSelectionProcessTestFormDTO.endDateTime());

        Integer quantityOfCorrectAnswers = solvedEvaluationTestQuestionService.countCorrectAnswers(solvedEvaluationTestQuestionList);
        solvedSelectionProcessTest.setQuantityOfCorrectAnswers(quantityOfCorrectAnswers);
        solvedSelectionProcessTest.setStudent(student);
        solvedSelectionProcessTest.setSolvedQuestionList(solvedEvaluationTestQuestionList);

        return solvedSelectionProcessTestRepository.save(solvedSelectionProcessTest);
    }

    public SolvedSelectionProcessTestDTO convertToSolvedSelectionProcessTestDTO(SolvedSelectionProcessTest solvedSelectionProcessTest) {
        return new SolvedSelectionProcessTestDTO(
                solvedSelectionProcessTest.getId(),
                solvedSelectionProcessTest.getStartDateTime(),
                solvedSelectionProcessTest.getEndDateTime(),
                solvedSelectionProcessTest.getQuantityOfCorrectAnswers(),
                updateImages(solvedSelectionProcessTest.getSolvedQuestionList())
        );
    }

    private List<SolvedEvaluationTestQuestion> updateImages(List<SolvedEvaluationTestQuestion> solvedQuestionList) {
        for (SolvedEvaluationTestQuestion solvedEvaluationTestQuestion : solvedQuestionList) {
            if (solvedEvaluationTestQuestion.getQuestion().getStatementImageUrl() != null) {
                String encodedImage = googleStorageService.encodeImageToBase64(solvedEvaluationTestQuestion.getQuestion().getStatementImageUrl());
                solvedEvaluationTestQuestion.getQuestion().setStatementImageUrl(encodedImage);
            }
        }

        return solvedQuestionList;
    }

    public Page<SolvedSelectionProcessTestDTO> list(Pageable pageable, SolvedSelectionProcessFilterDTO solvedSelectionProcessFilterDTO) {
        Specification<SolvedSelectionProcessTest> solvedSelectionProcessTestSpecification = generateSpecification(solvedSelectionProcessFilterDTO);

        Page<SolvedSelectionProcessTest> solvedSelectionProcessTests = solvedSelectionProcessTestRepository.findAll(solvedSelectionProcessTestSpecification, pageable);

        return solvedSelectionProcessTests.map(this::convertToSolvedSelectionProcessTestDTO);
    }

    private Specification<SolvedSelectionProcessTest> generateSpecification(SolvedSelectionProcessFilterDTO solvedSelectionProcessFilterDTO) {
        Search<Long> selectionProcessTestCriteria = SpecificationUtils.generateEqualsCriteria("selectionProcessTest.id", solvedSelectionProcessFilterDTO.selectionProcessTestId());
        Search<Long> studentCriteria = SpecificationUtils.generateEqualsCriteria("student.id", solvedSelectionProcessFilterDTO.studentId());

        Specification<SolvedSelectionProcessTest> selectionProcessTestSpecification = new BaseSpecification<>(selectionProcessTestCriteria);
        Specification<SolvedSelectionProcessTest> studentSpecification = new BaseSpecification<>(studentCriteria);

        return Specification.where(selectionProcessTestSpecification).and(studentSpecification);
    }

    public List<SolvedSelectionProcessTestDTO> list(SolvedSelectionProcessFilterDTO solvedSelectionProcessFilterDTO) {
        Specification<SolvedSelectionProcessTest> solvedSelectionProcessTestSpecification = generateSpecification(solvedSelectionProcessFilterDTO);
        List<SolvedSelectionProcessTest> selectionProcessTests = solvedSelectionProcessTestRepository.findAll(solvedSelectionProcessTestSpecification);

        return selectionProcessTests.stream().map(this::convertToSolvedSelectionProcessTestDTO).toList();
    }

    public SolvedSelectionProcessTest findById(Long id) {
        return solvedSelectionProcessTestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("SolvedSelectionProcessTest", "id", id.toString())
        );
    }
}
