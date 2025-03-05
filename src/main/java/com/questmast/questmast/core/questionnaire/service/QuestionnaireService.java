package com.questmast.questmast.core.questionnaire.service;

import com.questmast.questmast.core.google.service.GeminiService;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.questionnaire.domain.dto.QuestionnaireFormDTO;
import com.questmast.questmast.core.questionnaire.domain.model.Questionnaire;
import com.questmast.questmast.core.questionnaire.repository.QuestionnaireRepository;
import com.questmast.questmast.core.student.domain.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionnaireService {

    private final QuestionnaireRepository questionnaireRepository;

    public Questionnaire create(QuestionnaireFormDTO questionnaireFormDTO, Student student, List<Question> questionList) {
        Questionnaire questionnaire = new Questionnaire();
        questionnaire.setName(questionnaireFormDTO.name());
        questionnaire.setIsPublic(questionnaireFormDTO.isPublic());
        questionnaire.setStudent(student);
        questionnaire.setViewCounter(0);
        questionnaire.setQuestionList(questionList);

        return questionnaireRepository.save(questionnaire);
    }
}
