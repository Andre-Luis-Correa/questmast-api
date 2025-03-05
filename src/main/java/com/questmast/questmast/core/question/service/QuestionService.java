package com.questmast.questmast.core.question.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.common.exception.type.QuestionException;
import com.questmast.questmast.core.google.service.GeminiService;
import com.questmast.questmast.core.google.service.GoogleStorageService;
import com.questmast.questmast.core.question.domain.dto.QuestionFilterDTO;
import com.questmast.questmast.core.question.domain.dto.QuestionFormDTO;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.question.repository.QuestionRepository;
import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeFormDTO;
import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import com.questmast.questmast.core.questionalternative.service.QuestionAlternativeService;
import com.questmast.questmast.core.questiondifficultylevel.domain.entity.QuestionDifficultyLevel;
import com.questmast.questmast.core.questiondifficultylevel.service.QuestionDifficultyLevelService;
import com.questmast.questmast.core.questionnaire.domain.dto.QuestionnaireFormDTO;
import com.questmast.questmast.core.questionnaire.domain.dto.QuestionnaireQuestionFormDTO;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocess.service.SelectionProcessService;
import com.questmast.questmast.core.selectionprocesstest.domain.dto.SelectionProcessTestFormDTO;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.selectionprocesstest.service.SelectionProcessTestService;
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

import java.io.IOException;
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
    private final SelectionProcessService selectionProcessService;
    private final SelectionProcessTestService selectionProcessTestService;
    private final GeminiService geminiService;

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

    public List<Question> filterByDifficultyLevelAndSubjectAndSubjectTopic(List<Question> questionList, List<Long> questionDifficultyLevelIds, List<SubjectFilterDTO> subjectFilterDTOS) {
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


    public List<Question> filter(QuestionFilterDTO questionFilterDTO) {
        if(questionFilterDTO == null) {
            return questionRepository.findAll();
        }

        List<Question> questionList = new ArrayList<>();
        List<SelectionProcess> selectionProcesseList = selectionProcessService.findAllByBoardExaminerAndInstitution(questionFilterDTO.boardExaminerIds(), questionFilterDTO.institutionIds());

        if(!selectionProcesseList.isEmpty()) {
            List<SelectionProcessTest> selectionProcessTestList = selectionProcessTestService.findAllBySelectionProcessAndFunction(selectionProcesseList, questionFilterDTO.functionIds());
            questionList = selectionProcessTestList.stream().map(SelectionProcessTest::getQuestionList).flatMap(List::stream).toList();
            questionList = filterByDifficultyLevelAndSubjectAndSubjectTopic(questionList, questionFilterDTO.questionDifficultyLevelIds(), questionFilterDTO.subjectFilterDTOList());
        }

        return questionList;
    }

    public List<Question> getValidQuestionListForQuestionnaire(List<Question> questionList, QuestionnaireFormDTO questionnaireFormDTO) throws IOException, InterruptedException {
        List<Question> newQuestionList = new ArrayList<>();

        for(QuestionnaireQuestionFormDTO questionnaireQuestionFormDTO : questionnaireFormDTO.questionnaireQuestionFormDTOList()) {
            Subject subject = subjectService.findById(questionnaireQuestionFormDTO.subjectId());
            List<SubjectTopic> subjectTopicList = subjectTopicService.findAllByIdIn(questionnaireQuestionFormDTO.subjectTopicIds());
            QuestionDifficultyLevel questionDifficultyLevel = questionDifficultyLevelService.findById(questionnaireQuestionFormDTO.questionDifficultyLevelId());
            TestQuestionCategory testQuestionCategory = testQuestionCategoryService.findById(1L);

            String prompt = generateQuestionnairePrompt(questionList, subject, subjectTopicList, questionnaireQuestionFormDTO.quantity(), questionDifficultyLevel, testQuestionCategory);
            log.info(prompt);
            List<QuestionFormDTO> questionFormDTO = geminiService.generateQuestionsForQuestionnaire(prompt);

            List<Question> questionListGemini = convertToQuestionList(questionFormDTO, subject, subjectTopicList, questionDifficultyLevel, testQuestionCategory);

            newQuestionList.addAll(questionListGemini);
        }

        return newQuestionList;
    }

    private List<Question> convertToQuestionList(List<QuestionFormDTO> questionFormDTOList, Subject subject, List<SubjectTopic> subjectTopicList, QuestionDifficultyLevel questionDifficultyLevel, TestQuestionCategory testQuestionCategory) {
        List<Question> questionList = new ArrayList<>();

        for(QuestionFormDTO questionFormDTO : questionFormDTOList) {
            Set<SubjectTopic> subjectTopicSet = new HashSet<>(subjectTopicList);

            Question question = new Question();
            question.setApplicationDate(LocalDate.now());
            question.setStatement(questionFormDTO.statement());
            question.setExplanation(questionFormDTO.explanation());
            question.setVideoExplanationUrl(null);
            question.setQuestionDifficultyLevel(questionDifficultyLevel);
            question.setSubject(subject);
            question.setTestQuestionCategory(testQuestionCategory);
            question.setQuestionAlternativeList(generateQuestionAlternativeList(questionFormDTO.questionAlternativeList()));
            question.setSubjectTopicList(subjectTopicSet);
            question.setQuantityOfCorrectAnswers(0);
            question.setQuantityOfWrongAnswers(0);
            question.setQuantityOfTries(0);
            question.setName(questionFormDTO.name());

            questionList.add(question);
        }

        return questionList;
    }

    private String generateQuestionnairePrompt(
            List<Question> questionList,
            Subject subject,
            List<SubjectTopic> subjectTopicList,
            Integer quantity,
            QuestionDifficultyLevel questionDifficultyLevel,
            TestQuestionCategory testQuestionCategory
    ) {
        // 1. Defina um trecho com exemplos de questões existentes (ou um resumo delas)
        //    Assim a IA entende o estilo esperado.
        String sampleQuestionsSnippet = buildSampleQuestionsSnippet(questionList);

        // 2. Liste de forma clara os parâmetros que a IA deve levar em conta
        String subjectName = subject != null ? subject.getName() : "Disciplina não especificada";
        String difficultyName = questionDifficultyLevel != null
                ? questionDifficultyLevel.getName()
                : "Nível de dificuldade não especificado";
        String categoryName = testQuestionCategory != null
                ? testQuestionCategory.getName()
                : "Categoria não especificada";

        // Se você quiser detalhar os assuntos/tópicos (subjectTopicList)
        // para a IA, concatene os nomes deles em um String
        StringBuilder topicsBuilder = new StringBuilder();
        if (subjectTopicList != null && !subjectTopicList.isEmpty()) {
            for (SubjectTopic st : subjectTopicList) {
                topicsBuilder.append("- ").append(st.getName()).append("\n");
            }
        } else {
            topicsBuilder.append("Nenhum tópico específico informado.\n");
        }

        // 3. Montar instruções claras
        // Você pode usar placeholders, bullet points, etc.
        // Lembre-se de dizer “no máximo 10 por disciplina”
        String prompt =
                """
                Você é uma IA especializada em criar questões de prova.
        
                Por favor, gere até %d questões (no máximo 10) sobre a disciplina "%s" 
                na categoria "%s" e no nível de dificuldade "%s". 
                Selecione os tópicos (se existirem) e aborde-os de forma coerente:
        
                Tópicos sugeridos:
                %s
        
                As questões devem ser objetivas, cada uma com:
                - Enunciado claro
                - 4 ou 5 alternativas (A, B, C, D, E)
                - 1 alternativa correta
                - Estilo parecido com as questões já existentes 
                  (veja a seguir os exemplos que forneço)
        
                ***Exemplos de questões existentes (para referência de estilo):***
                %s
        
                Instruções adicionais:
                - Mantenha o mesmo idioma dos exemplos (caso estejam em Português).
                - Evite repetir o enunciado das perguntas de exemplo.
                - As novas questões devem ser originais, alinhadas aos tópicos e nível de dificuldade.
                - Respeite o limite: no máximo 10 questões.
                - Gere exatamente %d questões, se possível, com variação de assuntos dentro do tema.
                
                Formato de saída sugerido:
                1) [Enunciado da questão]
                   A) ...
                   B) ...
                   C) ...
                   D) ...
                   E) ...
                   Resposta correta: X
        
                2) [Enunciado da questão] 
                   ... e assim por diante.
        
                Obrigado!
                """.formatted(
                        // %d #1 -> quantidade solicitada
                        quantity != null || quantity < 10 ? quantity : 10,
                        // %s #2 -> nome da disciplina
                        subjectName,
                        // %s #3 -> categoria
                        categoryName,
                        // %s #4 -> nível de dificuldade
                        difficultyName,
                        // %s #5 -> tópicos
                        topicsBuilder.toString(),
                        // %s #6 -> trechos de exemplos
                        sampleQuestionsSnippet,
                        // %d #7 -> quantidade solicitada novamente
                        quantity != null ? quantity : 10
                );

        return prompt;
    }

    private String buildSampleQuestionsSnippet(List<Question> questionList) {
        if (questionList == null || questionList.isEmpty()) {
            return "Nenhuma questão de exemplo disponível.";
        }

        // Limitar a quantidade de exemplos, para não ficar muito verborrágico
        int maxExamples = Math.min(questionList.size(), 3);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxExamples; i++) {
            Question q = questionList.get(i);
            sb.append("Exemplo ").append(i + 1).append(":\n");
            sb.append("Enunciado: ").append(q.getStatement()).append("\n");
            // Se você quiser, pode colocar as alternativas.
            // Se a classe "QuestionAlternative" tiver 'getText()', adicione-as também:
            if (q.getQuestionAlternativeList() != null) {
                int altCount = 1;
                for (QuestionAlternative alt : q.getQuestionAlternativeList()) {
                    sb.append((char)('A' + altCount - 1)).append(") ")
                            .append(alt.getStatement()).append("\n");
                    altCount++;
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

}
