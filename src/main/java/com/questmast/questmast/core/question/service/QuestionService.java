package com.questmast.questmast.core.question.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.common.exception.type.QuestionException;
import com.questmast.questmast.core.google.service.GoogleStorageService;
import com.questmast.questmast.core.question.domain.dto.QuestionFormDTO;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.question.repository.QuestionRepository;
import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeFormDTO;
import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import com.questmast.questmast.core.questionalternative.service.QuestionAlternativeService;
import com.questmast.questmast.core.questiondifficultylevel.domain.entity.QuestionDifficultyLevel;
import com.questmast.questmast.core.questiondifficultylevel.service.QuestionDifficultyLevelService;
import com.questmast.questmast.core.selectionprocesstest.domain.dto.SelectionProcessTestFormDTO;
import com.questmast.questmast.core.subject.domain.dto.SubjectFilterDTO;
import com.questmast.questmast.core.subject.domain.entity.Subject;
import com.questmast.questmast.core.subject.service.SubjectService;
import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import com.questmast.questmast.core.subjecttopic.service.SubjectTopicService;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import com.questmast.questmast.core.testquestioncategory.service.TestQuestionCategoryService;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;

@Log4j2
@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionDifficultyLevelService questionDifficultyLevelService;
    private final SubjectService subjectService;
    private final TestQuestionCategoryService testQuestionCategoryService;
    private final SubjectTopicService subjectTopicService;
    private final QuestionAlternativeService questionAlternativeService;
    private final QuestionRepository questionRepository;
    private final GoogleStorageService googleStorageService;

    public Question findById(Long id) {
        return questionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("Question", "id", id.toString())
        );
    }

    public List<Question> getValidQuestionList(List<QuestionFormDTO> questionFormDTOS, LocalDate applicationDate) {
        List<Question> questionList = new ArrayList<>();

        for (QuestionFormDTO dto : questionFormDTOS) {
            QuestionDifficultyLevel questionDifficultyLevel = questionDifficultyLevelService.findById(dto.questionDifficultyLevelId());
            Subject subject = subjectService.findById(dto.subjectId());
            TestQuestionCategory testQuestionCategory = testQuestionCategoryService.findById(dto.testQuestionCategoryId());

            Question question = new Question();
            question.setApplicationDate(applicationDate);
            question.setStatement(dto.statement());
            question.setExplanation(dto.explanation());
            question.setVideoExplanationUrl(dto.videoExplanationUrl());
            question.setQuestionDifficultyLevel(questionDifficultyLevel);
            question.setSubject(subject);
            question.setTestQuestionCategory(testQuestionCategory);
            question.setQuestionAlternativeList(generateQuestionAlternativeList(dto.questionAlternativeList()));
            question.setSubjectTopicList(generateSubjectTopicList(subject, dto.subjectTopicList()));
            question.setQuantityOfCorrectAnswers(0);
            question.setQuantityOfWrongAnswers(0);
            question.setQuantityOfTries(0);
            question.setName(dto.name());

            if(dto.statementImage() != null && !dto.statementImage().isBlank()) {
                MultipartFile questionImage = googleStorageService.convertBase64ToMultipartFile(dto.statementImage(), "question_image.png");
                String uploadedFileName = googleStorageService.uploadImage(questionImage);
                question.setStatementImageUrl(uploadedFileName);
            }

            questionList.add(question);
        }

        return questionList;
    }

    private Set<SubjectTopic> generateSubjectTopicList(Subject subject, Set<Long> ids) {
        Set<SubjectTopic> subjectTopicList = new HashSet<>();

        for (Long id : ids) {
            SubjectTopic subjectTopic = subjectTopicService.findById(id);

            if(!subjectTopic.getSubject().equals(subject)) {
                throw new QuestionException("O tópico " + subjectTopic.getName() + " não pertence a disciplina " + subject.getName() + ".");
            }

            subjectTopicList.add(subjectTopicService.findById(id));
        }

        return subjectTopicList;
    }

    private List<QuestionAlternative> generateQuestionAlternativeList(List<QuestionAlternativeFormDTO> questionAlternativeFormDTOS) {
        List<QuestionAlternative> questionAlternativeList = new ArrayList<>();
        int correctAnswerCounter = 0;

        for (QuestionAlternativeFormDTO dto : questionAlternativeFormDTOS) {
            QuestionAlternative questionAlternative = new QuestionAlternative();

            questionAlternative.setStatement(dto.statement());
            questionAlternative.setIsCorrect(dto.isCorrect());
            if (dto.isCorrect()) {
                correctAnswerCounter++;
            }

            questionAlternativeList.add(questionAlternative);
        }

        if (correctAnswerCounter != 1) {
            throw new QuestionException("A questão deve ter ao menos uma alternativa correta.");
        }

        return questionAlternativeList;
    }

    public List<Question> updateQuestionList(List<Question> selectionProcessQuestions, List<QuestionFormDTO> questionUpdateDTOS, SelectionProcessTestFormDTO selectionProcessTestFormDTO) {
        List<Question> questionList = new ArrayList<>();

        for (QuestionFormDTO dto : questionUpdateDTOS) {
            QuestionDifficultyLevel questionDifficultyLevel = questionDifficultyLevelService.findById(dto.questionDifficultyLevelId());
            Subject subject = subjectService.findById(dto.subjectId());
            TestQuestionCategory testQuestionCategory = testQuestionCategoryService.findById(dto.testQuestionCategoryId());

            Question question = new Question();
            if (dto.id() != null) {
                question = findById(dto.id());

                if (!selectionProcessQuestions.contains(question)) {
                    throw new QuestionException("Questão com id " + question.getId() + " não pertence ao processo seletivo.");
                }
            } else {
                question.setQuantityOfCorrectAnswers(0);
                question.setQuantityOfWrongAnswers(0);
                question.setQuantityOfTries(0);
            }

            if(dto.statementImage() != null) {
                if(question.getStatementImageUrl() != null) {
                    googleStorageService.removeOldImage(question.getStatementImageUrl());
                }
                MultipartFile questionImage = googleStorageService.convertBase64ToMultipartFile(dto.statementImage(), "question_image.png");
                String uploadedFileName = googleStorageService.uploadImage(questionImage);
                question.setStatementImageUrl(uploadedFileName);
            } else {
                if(question.getStatementImageUrl() != null) {
                    googleStorageService.removeOldImage(question.getStatementImageUrl());
                }
            }

            question.setApplicationDate(selectionProcessTestFormDTO.applicationDate());
            question.setStatement(dto.statement());
            question.setExplanation(dto.explanation());
            question.setVideoExplanationUrl(dto.videoExplanationUrl());
            question.setQuestionDifficultyLevel(questionDifficultyLevel);
            question.setSubject(subject);
            question.setTestQuestionCategory(testQuestionCategory);
            question.setSubjectTopicList(generateSubjectTopicList(subject, dto.subjectTopicList()));
            question.setQuestionAlternativeList(updateAlternativeList(question, dto.questionAlternativeList()));
            question.setName(dto.name());

            questionList.add(questionRepository.save(question));
        }

        return questionList;
    }

    private List<QuestionAlternative> updateAlternativeList(Question question, List<QuestionAlternativeFormDTO> questionAlternativeFormDTOS) {
        List<QuestionAlternative> questionAlternativeList = new ArrayList<>();

        for (QuestionAlternativeFormDTO dto : questionAlternativeFormDTOS) {
            QuestionAlternative questionAlternative = new QuestionAlternative();

            if (dto.id() != null) {
                questionAlternative = questionAlternativeService.findById(dto.id());

                if (question.getId() != null && !question.getQuestionAlternativeList().contains(questionAlternative)) {
                    throw new QuestionException("Alternativa com id " + questionAlternative.getId() + " não pertence a questão com id " + question.getId() + ".");
                }
            }

            questionAlternative.setStatement(dto.statement());
            questionAlternative.setIsCorrect(dto.isCorrect());

            questionAlternativeList.add(questionAlternativeService.save(questionAlternative));
        }

        return questionAlternativeList;
    }

    public void deleteUnusedQuestions(@NotEmpty List<Question> questionList, List<Question> questionListUpdated) {
        log.info(questionList);
        log.info(questionListUpdated);
        List<Long> ids = questionListUpdated.stream().map(Question::getId).toList();
        for(Question question : questionList) {
            if(!ids.contains(question.getId())) {
                log.info("Question being deleted: {}", question.getId());

                if(question.getStatementImageUrl() != null && !question.getStatementImageUrl().isBlank()) {
                    googleStorageService.removeOldImage(question.getStatementImageUrl());
                }
                questionRepository.delete(question);
            }
        }
    }

    public List<Question> findAllByQuestionsAndDifficultyLevelAndSubjectAndSubjectTopic(List<Question> questionList, List<Long> questionDifficultyLevelIds, List<SubjectFilterDTO> subjectFilterDTOS) {
        if (questionList == null) {
            return Collections.emptyList();
        }
        if (questionDifficultyLevelIds == null) {
            questionDifficultyLevelIds = Collections.emptyList();
        }
        if (subjectFilterDTOS == null) {
            subjectFilterDTOS = Collections.emptyList();
        }

        final List<Long> finalQuestionDifficultyLevelIds = questionDifficultyLevelIds;
        final List<SubjectFilterDTO> finalSubjectFilterDTOS = subjectFilterDTOS;

        return questionList.stream()
                .filter(q -> filterByDifficulty(q, finalQuestionDifficultyLevelIds))
                .filter(q -> filterBySubjectAndTopics(q, finalSubjectFilterDTOS))
                .toList();
    }

    private boolean filterByDifficulty(Question question, List<Long> difficultyIds) {
        if (difficultyIds.isEmpty()) {
            return true;
        }
        return difficultyIds.contains(question.getQuestionDifficultyLevel().getId());
    }

    private boolean filterBySubjectAndTopics(Question question, List<SubjectFilterDTO> subjectFilters) {
        if (subjectFilters.isEmpty()) {
            return true;
        }

        return subjectFilters.stream().anyMatch(sf -> matchesSubjectFilter(question, sf));
    }

    private boolean matchesSubjectFilter(Question question, SubjectFilterDTO sf) {
        if (sf.subjectId() != null && !sf.subjectId().equals(question.getSubject().getId())) {
            return false;
        }

        if (sf.subjectTopicIds() != null && !sf.subjectTopicIds().isEmpty()) {
            boolean foundAtLeastOne =
                    question.getSubjectTopicList().stream()
                            .anyMatch(st -> sf.subjectTopicIds().contains(st.getId()));
            if (!foundAtLeastOne) {
                return false;
            }
        }

        return true;
    }


}
