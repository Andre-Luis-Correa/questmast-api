package com.questmast.questmast.core.questionnaire.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.google.service.GeminiService;
import com.questmast.questmast.core.google.service.GoogleStorageService;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.questionnaire.domain.dto.QuestionnaireFormDTO;
import com.questmast.questmast.core.questionnaire.domain.dto.QuestionnaireQuestionFormDTO;
import com.questmast.questmast.core.questionnaire.domain.model.Questionnaire;
import com.questmast.questmast.core.questionnaire.repository.QuestionnaireRepository;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.student.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;
    private final GoogleStorageService googleStorageService;
    private final Random random = new Random();

    public Questionnaire create(QuestionnaireFormDTO questionnaireFormDTO, Student student, List<Question> questionList) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(questionnaireFormDTO.name());
        questionnaire.setIsPublic(questionnaireFormDTO.isPublic());
        questionnaire.setStudent(student);
        questionnaire.setViewCounter(0);
        questionnaire.setQuestionList(questionList);

        return questionnaireRepository.save(questionnaire);
    }

    public Questionnaire findById(Long id) {
        return questionnaireRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("Questionnaire", "id", id.toString())
        );
    }

    public void delete(Questionnaire questionnaire) {
        questionnaireRepository.delete(questionnaire);
    }

    public List<Questionnaire> findAll() {
        return questionnaireRepository.findAll();
    }

    public Page<Questionnaire> findAll(Pageable pageable) {
        return questionnaireRepository.findAll(pageable);
    }

    public Questionnaire createRandomQuestionnaire(QuestionnaireFormDTO questionnaireFormDTO, Student student, List<Question> questionList) {
        List<Question> selectedQuestions = new ArrayList<>();

        for (QuestionnaireQuestionFormDTO questionForm : questionnaireFormDTO.questionnaireQuestionFormDTOList()) {
            List<Question> subjectQuestions = questionList.stream()
                    .filter(q -> q.getSubject().getId().equals(questionForm.subjectId()) &&
                            q.getSubjectTopicList().stream().anyMatch(topic -> questionForm.subjectTopicIds().contains(topic.getId())) &&
                            q.getQuestionDifficultyLevel().getId().equals(questionForm.questionDifficultyLevelId()))
                    .collect(Collectors.toList());

            if (!subjectQuestions.isEmpty()) {
                Collections.shuffle(subjectQuestions, random);
                int quantityToSelect = Math.min(subjectQuestions.size(), questionForm.quantity());
                selectedQuestions.addAll(subjectQuestions.subList(0, quantityToSelect));
            }
        }

        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(questionnaireFormDTO.name());
        questionnaire.setIsPublic(questionnaireFormDTO.isPublic());
        questionnaire.setStudent(student);
        questionnaire.setViewCounter(0);
        questionnaire.setQuestionList(selectedQuestions);

        return questionnaireRepository.save(questionnaire);
    }

    public Questionnaire updateViewCounter(Questionnaire questionnaire) {
        questionnaire.setViewCounter(questionnaire.getViewCounter() + 1);

        return questionnaireRepository.save(questionnaire);
    }

    public void insertEncodedImages(List<Questionnaire> questionnaires) {
        for(Questionnaire questionnaire : questionnaires) {
            for(Question question : questionnaire.getQuestionList()) {
                if(question.getStatementImageUrl() != null) {
                    String encodedImage = googleStorageService.encodeImageToBase64(question.getStatementImageUrl());
                    question.setStatementImageUrl(encodedImage);
                }
            }
        }
    }

    public Questionnaire findByIdOrNull(Long id) {
        if(id == null) return null;
        return findById(id);
    }
}
