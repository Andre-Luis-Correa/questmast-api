package com.questmast.questmast.core.solvedquestionnaire.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.common.specification.BaseSpecification;
import com.questmast.questmast.common.specification.Search;
import com.questmast.questmast.common.specification.SpecificationUtils;
import com.questmast.questmast.core.google.service.GoogleStorageService;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.questionnaire.domain.model.Questionnaire;
import com.questmast.questmast.core.solvedevaluationtestquestion.domain.model.SolvedEvaluationTestQuestion;
import com.questmast.questmast.core.solvedevaluationtestquestion.service.SolvedEvaluationTestQuestionService;
import com.questmast.questmast.core.solvedquestionnaire.domain.dto.SolvedQuestionnaireDTO;
import com.questmast.questmast.core.solvedquestionnaire.domain.dto.SolvedQuestionnaireFilterDTO;
import com.questmast.questmast.core.solvedquestionnaire.domain.dto.SolvedQuestionnaireFormDTO;
import com.questmast.questmast.core.solvedquestionnaire.domain.model.SolvedQuestionnaire;
import com.questmast.questmast.core.solvedquestionnaire.repository.SolvedQuestionnaireRepository;
import com.questmast.questmast.core.student.domain.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SolvedQuestionnaireService {

    private final SolvedEvaluationTestQuestionService solvedEvaluationTestQuestionService;
    private final SolvedQuestionnaireRepository solvedQuestionnaireRepository;
    private final GoogleStorageService googleStorageService;

    public SolvedQuestionnaire create(Questionnaire questionnaire, SolvedQuestionnaireFormDTO solvedQuestionnaireFormDTO, Student student, List<SolvedEvaluationTestQuestion> solvedEvaluationTestQuestionList) {
        SolvedQuestionnaire solvedQuestionnaire = new SolvedQuestionnaire();
        solvedQuestionnaire.setQuestionnaire(questionnaire);
        solvedQuestionnaire.setStartDateTime(solvedQuestionnaireFormDTO.startDateTime());
        solvedQuestionnaire.setEndDateTime(solvedQuestionnaireFormDTO.endDateTime());

        Integer quantityOfCorrectAnswers = solvedEvaluationTestQuestionService.countCorrectAnswers(solvedEvaluationTestQuestionList);
        solvedQuestionnaire.setQuantityOfCorrectAnswers(quantityOfCorrectAnswers);
        solvedQuestionnaire.setStudent(student);
        solvedQuestionnaire.setSolvedQuestionList(solvedEvaluationTestQuestionList);

        return solvedQuestionnaireRepository.save(solvedQuestionnaire);
    }

    public SolvedQuestionnaireDTO convertToQuestionnaireTestDTO(SolvedQuestionnaire solvedQuestionnaire) {
        return new SolvedQuestionnaireDTO(
                solvedQuestionnaire.getId(),
                solvedQuestionnaire.getStartDateTime(),
                solvedQuestionnaire.getEndDateTime(),
                solvedQuestionnaire.getQuantityOfCorrectAnswers(),
                updateImages(solvedQuestionnaire.getSolvedQuestionList())
        );
    }

    private List<SolvedEvaluationTestQuestion> updateImages(List<SolvedEvaluationTestQuestion> solvedQuestionList) {
        return solvedQuestionList.stream()
                .map(solvedEvaluationTestQuestion -> {
                    Question question = solvedEvaluationTestQuestion.getQuestion();

                    if (question.getStatementImageUrl() != null) {

                        String encodedImage = googleStorageService.encodeImageToBase64(question.getStatementImageUrl());
                        Question updatedQuestion = new Question(
                                question.getId(),
                                question.getApplicationDate(),
                                question.getIsGeneratedByAi(),
                                question.getName(),
                                encodedImage,
                                question.getStatementImageLegend(),
                                question.getStatement(),
                                question.getQuantityOfCorrectAnswers(),
                                question.getQuantityOfWrongAnswers(),
                                question.getQuantityOfTries(),
                                question.getExplanation(),
                                question.getVideoExplanationUrl(),
                                question.getQuestionAlternativeList(),
                                question.getQuestionDifficultyLevel(),
                                question.getSubject(),
                                question.getSubjectTopicList()
                        );
                        return new SolvedEvaluationTestQuestion(
                                solvedEvaluationTestQuestion.getId(),
                                solvedEvaluationTestQuestion.getIsCorrect(),
                                solvedEvaluationTestQuestion.getStartDateTime(),
                                solvedEvaluationTestQuestion.getEndDateTime(),
                                solvedEvaluationTestQuestion.getQuestionAlternative(),
                                updatedQuestion
                        );

                    }

                    return solvedEvaluationTestQuestion;
                })
                .toList();
    }

    public SolvedQuestionnaire findById(Long id) {
        return solvedQuestionnaireRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("SolvedQuestionnaire", "id", id.toString())
        );
    }

    public SolvedQuestionnaire findLastByStudentAndQuestionnaire(Student student, Questionnaire questionnaire) {
        if(student == null || questionnaire == null) return null;
        return solvedQuestionnaireRepository.findFirstByStudentAndQuestionnaireOrderByEndDateTimeDesc(student, questionnaire).orElse(null);
    }

    public List<SolvedQuestionnaireDTO> list(SolvedQuestionnaireFilterDTO solvedQuestionnaireFilterDTO) {
        Specification<SolvedQuestionnaire> solvedQuestionnaireSpecification = generateSpecification(solvedQuestionnaireFilterDTO);

        Sort sort = Sort.by(Sort.Direction.DESC, "endDateTime");

        List<SolvedQuestionnaire> solvedQuestionnaires = solvedQuestionnaireRepository.findAll(solvedQuestionnaireSpecification, sort);

        return solvedQuestionnaires.stream().map(this::convertToQuestionnaireTestDTO).toList();

    }

    public Page<SolvedQuestionnaireDTO> list(Pageable pageable, SolvedQuestionnaireFilterDTO solvedQuestionnaireFilterDTO) {
        Specification<SolvedQuestionnaire> solvedQuestionnaireSpecification = generateSpecification(solvedQuestionnaireFilterDTO);

        if (pageable.getSort().isUnsorted()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by(Sort.Direction.DESC, "endDateTime")
            );
        }

        Page<SolvedQuestionnaire> solvedQuestionnaires = solvedQuestionnaireRepository.findAll(solvedQuestionnaireSpecification, pageable);

        return solvedQuestionnaires.map(this::convertToQuestionnaireTestDTO);

    }

    private Specification<SolvedQuestionnaire> generateSpecification(SolvedQuestionnaireFilterDTO solvedQuestionnaireFilterDTO) {
        Search<Long> questionnaireCriteria = SpecificationUtils.generateEqualsCriteria("questionnaire.id", solvedQuestionnaireFilterDTO.questionnaireId());
        Search<String> studentCriteria = SpecificationUtils.generateEqualsCriteria("student.mainEmail", solvedQuestionnaireFilterDTO.studentMainEmail());

        Specification<SolvedQuestionnaire> questionnaireSpecification = new BaseSpecification<>(questionnaireCriteria);
        Specification<SolvedQuestionnaire> studentSpecification = new BaseSpecification<>(studentCriteria);

        return Specification.where(questionnaireSpecification).and(studentSpecification);
    }
}
