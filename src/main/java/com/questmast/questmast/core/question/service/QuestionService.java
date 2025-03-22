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
import com.questmast.questmast.core.solvedevaluationtestquestion.domain.model.SolvedEvaluationTestQuestion;
import com.questmast.questmast.core.subject.domain.dto.SubjectFilterDTO;
import com.questmast.questmast.core.subject.domain.entity.Subject;
import com.questmast.questmast.core.subject.service.SubjectService;
import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import com.questmast.questmast.core.subjecttopic.service.SubjectTopicService;
import com.questmast.questmast.core.testquestioncategory.service.TestQuestionCategoryService;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

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

            Question question = new Question();
            question.setApplicationDate(applicationDate);
            question.setStatement(dto.statement());
            question.setExplanation(dto.explanation());
            question.setVideoExplanationUrl(dto.videoExplanationUrl());
            question.setQuestionDifficultyLevel(questionDifficultyLevel);
            question.setSubject(subject);
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
                question.setStatementImageLegend(dto.statementImageLegend());
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
                question.setStatementImageLegend(dto.statementImageLegend());
            } else {
                if(question.getStatementImageUrl() != null) {
                    googleStorageService.removeOldImage(question.getStatementImageUrl());
                    question.setStatementImageUrl(null);
                    question.setStatementImageLegend(null);
                }
            }

            question.setApplicationDate(selectionProcessTestFormDTO.applicationDate());
            question.setStatement(dto.statement());
            question.setExplanation(dto.explanation());
            question.setVideoExplanationUrl(dto.videoExplanationUrl());
            question.setQuestionDifficultyLevel(questionDifficultyLevel);
            question.setSubject(subject);
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

    public List<Question> filterByDifficultyLevelAndSubjectAndSubjectTopic(
            List<Question> questionList, List<Long> questionDifficultyLevelIds, List<SubjectFilterDTO> subjectFilterDTOS) {

        if (questionList == null) {
            return Collections.emptyList();
        }

        boolean filterByDifficulty = questionDifficultyLevelIds != null && !questionDifficultyLevelIds.isEmpty();
        boolean filterBySubjects = subjectFilterDTOS != null && !subjectFilterDTOS.isEmpty();

        // Se ambos forem vazios ou nulos, retorna todas as questões sem filtragem
        if (!filterByDifficulty && !filterBySubjects) {
            return questionList;
        }

        return questionList.stream()
                .filter(q -> !filterByDifficulty || filterByDifficulty(q, questionDifficultyLevelIds))
                .filter(q -> !filterBySubjects || filterBySubjectAndTopics(q, subjectFilterDTOS))
                .toList();
    }

    private boolean filterByDifficulty(Question question, List<Long> difficultyIds) {
        if (difficultyIds == null || difficultyIds.isEmpty()) {
            return true; // Se a dificuldade não foi informada, aceita todas as questões
        }
        return difficultyIds.contains(question.getQuestionDifficultyLevel().getId());
    }

    private boolean filterBySubjectAndTopics(Question question, List<SubjectFilterDTO> subjectFilters) {
        if (subjectFilters == null || subjectFilters.isEmpty()) {
            return true; // Se nenhuma disciplina foi informada, aceita todas as questões
        }

        return subjectFilters.stream().anyMatch(sf -> matchesSubjectFilter(question, sf));
    }

    private boolean matchesSubjectFilter(Question question, SubjectFilterDTO sf) {
        // Se a disciplina foi informada e não corresponde, descarta a questão
        if (sf.subjectId() != null && !sf.subjectId().equals(question.getSubject().getId())) {
            return false;
        }

        // Se os tópicos não forem informados, aceita todas as questões dessa disciplina
        if (sf.subjectTopicIds() == null || sf.subjectTopicIds().isEmpty()) {
            return true;
        }

        // Se os tópicos foram informados, verifica se pelo menos um bate
        return question.getSubjectTopicList().stream()
                .anyMatch(st -> sf.subjectTopicIds().contains(st.getId()));
    }

    public List<Question> filter(QuestionFilterDTO questionFilterDTO) {
        if(questionFilterDTO == null) {
            return questionRepository.findByIsGeneratedByAiFalse();
        }

        List<Question> questionList = new ArrayList<>();
        List<SelectionProcess> selectionProcesseList = selectionProcessService.findAllByBoardExaminerAndInstitution(questionFilterDTO.boardExaminerIds(), questionFilterDTO.institutionIds());

        if(!selectionProcesseList.isEmpty()) {
            List<SelectionProcessTest> selectionProcessTestList = selectionProcessTestService.findAllBySelectionProcessAndFunction(selectionProcesseList, questionFilterDTO.functionIds());
            questionList = selectionProcessTestList.stream().map(SelectionProcessTest::getQuestionList).flatMap(List::stream).filter(q -> !q.getIsGeneratedByAi()).toList();
            questionList = filterByDifficultyLevelAndSubjectAndSubjectTopic(questionList, questionFilterDTO.questionDifficultyLevelIds(), questionFilterDTO.subjectFilterDTOList());
        }

        return questionList;
    }

    public List<Question> getValidQuestionListForQuestionnaire(List<Question> questionList, QuestionnaireFormDTO questionnaireFormDTO) throws IOException, InterruptedException {
        List<Question> newQuestionList = new ArrayList<>();

        for(QuestionnaireQuestionFormDTO questionnaireQuestionFormDTO : questionnaireFormDTO.questionnaireQuestionFormDTOList()) {
            Subject subject = subjectService.findById(questionnaireQuestionFormDTO.subjectId());
            List<SubjectTopic> subjectTopicList = subjectTopicService.findAllByIdIn(questionnaireQuestionFormDTO.subjectTopicIds());
            List<QuestionDifficultyLevel> questionDifficultyLevelList = questionDifficultyLevelService.findAllByIdIn(questionnaireQuestionFormDTO.questionDifficultyLevelIds());

            String prompt = generateQuestionnairePrompt(questionList, subject, subjectTopicList, questionnaireQuestionFormDTO.quantity(), questionDifficultyLevelList);
            log.info(prompt);
            List<QuestionFormDTO> questionFormDTO = geminiService.generateQuestionsForQuestionnaire(prompt);

            List<Question> questionListGemini = convertToQuestionList(questionFormDTO, subject, subjectTopicList);

            newQuestionList.addAll(questionListGemini);
        }

        return newQuestionList;
    }

    private List<Question> convertToQuestionList(List<QuestionFormDTO> questionFormDTOList, Subject subject, List<SubjectTopic> subjectTopicList) {
        List<Question> questionList = new ArrayList<>();

        for(QuestionFormDTO questionFormDTO : questionFormDTOList) {
            QuestionDifficultyLevel questionDifficultyLevel = questionDifficultyLevelService.findById(questionFormDTO.questionDifficultyLevelId());

            Set<SubjectTopic> subjectTopicSet = new HashSet<>(subjectTopicList);

            Question question = new Question();
            question.setApplicationDate(LocalDate.now());
            question.setIsGeneratedByAi(true);
            question.setStatement(questionFormDTO.statement());
            question.setExplanation(questionFormDTO.explanation());
            question.setVideoExplanationUrl(null);
            question.setQuestionDifficultyLevel(questionDifficultyLevel);
            question.setSubject(subject);
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
            List<QuestionDifficultyLevel> questionDifficultyLevelList // Agora recebemos uma lista de dificuldades
    ) {
        String sampleQuestionsSnippet = buildSampleQuestionsSnippet(questionList);

        String subjectName = (subject != null) ? subject.getName() : "Disciplina não especificada";
        String categoryName = "Questões objetivas";

        // 🔹 Monta a lista de dificuldades em uma string amigável
        String difficultyNames;
        if (questionDifficultyLevelList != null && !questionDifficultyLevelList.isEmpty()) {
            difficultyNames = questionDifficultyLevelList.stream()
                    .sorted(Comparator.comparing(QuestionDifficultyLevel::getId)) // Ordena pelo ID
                    .map(difficulty -> difficulty.getId() + " - " + difficulty.getName()) // Exibe ID e Nome
                    .collect(Collectors.joining(", "));
        } else {
            difficultyNames = "Nível de dificuldade não especificado";
        }

        // 🔹 Monta os tópicos em uma string amigável
        StringBuilder topicsBuilder = new StringBuilder();
        if (subjectTopicList != null && !subjectTopicList.isEmpty()) {
            for (SubjectTopic st : subjectTopicList) {
                topicsBuilder.append("- ").append(st.getName()).append("\n");
            }
        } else {
            topicsBuilder.append("Nenhum tópico específico informado.\n");
        }

        // 🔹 Gera o novo prompt considerando múltiplos níveis de dificuldade
        String prompt =
                """
                Você é uma IA especializada em criar questões de prova.
    
                Por favor, gere até %d questões (no máximo 10) sobre a disciplina "%s" 
                na categoria "%s" e considerando os seguintes níveis de dificuldade: **%s**.
                
                As questões devem variar entre esses níveis e abordar os tópicos informados.
                
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
                - As questões geradas devem ser equilibradas entre os níveis de dificuldade fornecidos.
                - Evite repetir enunciados semelhantes às perguntas de exemplo.
                - Gere um conjunto diversificado de questões que cubram diferentes tópicos.
                - Respeite o limite: no máximo 10 questões.
                - Produza exatamente %d questões, se possível.
                
                Formato de saída sugerido:
                1) [Enunciado da questão] (Dificuldade: [Fácil, Média ou Difícil])
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
                        (quantity != null || quantity < 10) ? quantity : 10, // Número de questões
                        subjectName, // Nome da disciplina
                        categoryName, // Categoria
                        difficultyNames, // Lista de níveis de dificuldade formatados
                        topicsBuilder.toString(), // Lista de tópicos formatada
                        sampleQuestionsSnippet, // Exemplos de questões existentes
                        quantity != null ? quantity : 10 // Quantidade de questões novamente
                );

        return prompt;
    }

    private String buildSampleQuestionsSnippet(List<Question> questionList) {
        if (questionList == null || questionList.isEmpty()) {
            return "Nenhuma questão de exemplo disponível.";
        }

        int maxExamples = Math.min(questionList.size(), 3);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < maxExamples; i++) {
            Question q = questionList.get(i);
            sb.append("Exemplo ").append(i + 1).append(":\n");
            sb.append("Enunciado: ").append(q.getStatement()).append("\n");

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

    @Transactional
    public void updateQuestionInformationAfterSolved(List<SolvedEvaluationTestQuestion> solvedEvaluationTestQuestions) {
        List<Question> updatedQuestions = new ArrayList<>();

        for (SolvedEvaluationTestQuestion solvedQuestion : solvedEvaluationTestQuestions) {
            Question question = solvedQuestion.getQuestion();
            question.setQuantityOfTries(question.getQuantityOfTries() + 1);

            if (Boolean.TRUE.equals(solvedQuestion.getIsCorrect())) {
                question.setQuantityOfCorrectAnswers(question.getQuantityOfCorrectAnswers() + 1);
            } else {
                question.setQuantityOfWrongAnswers(question.getQuantityOfWrongAnswers() + 1);
            }

            updatedQuestions.add(question);
        }

        questionRepository.saveAll(updatedQuestions);
    }
}
