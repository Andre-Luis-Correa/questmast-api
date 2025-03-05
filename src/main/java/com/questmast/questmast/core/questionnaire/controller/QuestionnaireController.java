package com.questmast.questmast.core.questionnaire.controller;

import com.questmast.questmast.core.google.service.GeminiService;
import com.questmast.questmast.core.question.domain.dto.QuestionFormDTO;
import com.questmast.questmast.core.questiondifficultylevel.domain.entity.QuestionDifficultyLevel;
import com.questmast.questmast.core.questiondifficultylevel.service.QuestionDifficultyLevelService;
import com.questmast.questmast.core.questionnaire.domain.dto.QuestionnaireFormDTO;
import com.questmast.questmast.core.questionnaire.domain.dto.QuestionnaireQuestionFormDTO;
import com.questmast.questmast.core.questionnaire.service.QuestionnaireService;
import com.questmast.questmast.core.subject.domain.entity.Subject;
import com.questmast.questmast.core.subject.service.SubjectService;
import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import com.questmast.questmast.core.subjecttopic.service.SubjectTopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/questionnaire")
public class QuestionnaireController {

    private final QuestionnaireService questionnaireService;
    private final GeminiService geminiService;
    private final SubjectService subjectService;
    private final SubjectTopicService subjectTopicService;
    private final QuestionDifficultyLevelService questionDifficultyLevelService;

//    @PostMapping("/gemini")
//    public ResponseEntity<List<QuestionFormDTO>> getQuestionsFromGemini(@Valid @RequestBody QuestionnaireFormDTO questionnaireFormDTO) {
//        List<QuestionFormDTO> questionFormDTOList = new ArrayList<>();
//
//
//
//        for(QuestionnaireQuestionFormDTO questionnaireQuestionFormDTO : questionnaireFormDTO.questionnaireQuestionFormDTOList()) {
//            Subject subject = subjectService.findById(questionnaireQuestionFormDTO.subjectId());
//            List<SubjectTopic> subjectTopicList = subjectTopicService.findAllByIdIn(questionnaireQuestionFormDTO.subjectTopicIds());
//            QuestionDifficultyLevel questionDifficultyLevel = questionDifficultyLevelService.findById(questionnaireQuestionFormDTO.questionDifficultyLevelId());
//
//            List<QuestionFormDTO> questionFormDTOS = geminiService.getQuestionsByContentAndProvidedStyle();
//        }
//    }

}
