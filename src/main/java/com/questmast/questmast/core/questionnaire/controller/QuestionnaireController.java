package com.questmast.questmast.core.questionnaire.controller;

import com.questmast.questmast.common.exception.type.QuestionException;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.question.service.QuestionService;
import com.questmast.questmast.core.questionnaire.domain.dto.QuestionnaireFormDTO;
import com.questmast.questmast.core.questionnaire.domain.model.Questionnaire;
import com.questmast.questmast.core.questionnaire.service.QuestionnaireService;
import com.questmast.questmast.core.student.domain.Student;
import com.questmast.questmast.core.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;
    private final QuestionService questionService;
    private final StudentService studentService;

    @PostMapping("/gemini")
    public ResponseEntity<Questionnaire> createWithGemini(@Valid @RequestBody QuestionnaireFormDTO questionnaireFormDTO) throws IOException, InterruptedException {
        List<Question> questionList = questionService.filter(questionnaireFormDTO.questionFilterDTO());
        if(questionnaireFormDTO.questionFilterDTO() != null && questionList.isEmpty()) throw new QuestionException("Não foi possível gerar o questionário com os filtros aplicados, pois nenhuma questão foi encontrada para replicar o estilo.");

        Student student = studentService.findByMainEmail(questionnaireFormDTO.studentEmail());
        questionList = questionService.getValidQuestionListForQuestionnaire(questionList, questionnaireFormDTO);
        log.info(questionList);
        Questionnaire questionnaire = questionnaireService.create(questionnaireFormDTO, student, questionList);

        return ResponseEntity.status(HttpStatus.CREATED).body(questionnaire);
    }
}
