package com.questmast.questmast.core.performance.service;

import com.questmast.questmast.core.google.service.GeminiService;
import com.questmast.questmast.core.performance.domain.dto.*;
import com.questmast.questmast.core.performance.domain.model.Performance;
import com.questmast.questmast.core.solvedevaluationtestquestion.domain.model.SolvedEvaluationTestQuestion;
import com.questmast.questmast.core.solvedquestionnaire.domain.dto.SolvedQuestionnaireDTO;
import com.questmast.questmast.core.solvedquestionnaire.domain.model.SolvedQuestionnaire;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.dto.SolvedSelectionProcessTestDTO;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.model.SolvedSelectionProcessTest;
import com.questmast.questmast.core.student.domain.model.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PerformanceService {

    private final GeminiService geminiService;

    public StudentPerformanceDTO getStudentPerformance(Student student, List<SolvedQuestionnaire> solvedQuestionnaires, List<SolvedSelectionProcessTest> solvedSelectionProcessTestList) {

        List<SolvedEvaluationTestQuestion> allSolvedQuestions = mergeAllSolvedQuestions(
                solvedQuestionnaires,
                solvedSelectionProcessTestList
        );

        // 3) Número de questionários e processos seletivos respondidos
        int numberOfQuestionnaires = solvedQuestionnaires.size();
        int numberOfSelectionProcessTests = solvedSelectionProcessTestList.size();

        // 4) Cálculos gerais (tempo médio, percentual de acertos, etc.)
        PerformanceGeneralStatsDTO generalStats = calculateGeneralStats(
                allSolvedQuestions,
                numberOfQuestionnaires,
                numberOfSelectionProcessTests
        );

        // 5) Estatísticas por mês (quantidade de questões, acertos/erros)
        List<MonthYearCountDTO> questionsPerMonth = getQuestionsPerMonth(allSolvedQuestions);
        List<MonthYearCorrectIncorrectDTO> correctIncorrectPerMonth = getCorrectIncorrectPerMonth(allSolvedQuestions);

        // 6) Desempenho por disciplina (Subject)
        List<SubjectPerformanceDTO> subjectPerformanceList = getSubjectPerformance(allSolvedQuestions);

        // 7) Separar topSubjects e bottomSubjects (por maior/menor % de acertos)
        //    Aqui, vamos ordenar e criar duas listas distintas
        subjectPerformanceList.sort(Comparator.comparingDouble(SubjectPerformanceDTO::correctPercentage).reversed());
        List<SubjectPerformanceDTO> topSubjects = new ArrayList<>(subjectPerformanceList);
        List<SubjectPerformanceDTO> bottomSubjects = new ArrayList<>(subjectPerformanceList);
        Collections.reverse(bottomSubjects);

        // 8) Montar o DTO final
        return new StudentPerformanceDTO(
                generalStats.averageResponseTimeInSeconds(),
                generalStats.numberOfQuestionnaires(),
                generalStats.numberOfSelectionProcessTests(),
                generalStats.overallCorrectPercentage(),
                questionsPerMonth,
                correctIncorrectPerMonth,
                topSubjects,
                bottomSubjects
        );
    }

    private List<SolvedEvaluationTestQuestion> mergeAllSolvedQuestions(
            List<SolvedQuestionnaire> solvedQuestionnaireList,
            List<SolvedSelectionProcessTest> solvedSelectionProcessTestList
    ) {
        List<SolvedEvaluationTestQuestion> allSolvedQuestions = new ArrayList<>();

        // Adiciona as questões dos questionários
        for (SolvedQuestionnaire sq : solvedQuestionnaireList) {
            allSolvedQuestions.addAll(sq.getSolvedQuestionList());
        }

        // Adiciona as questões dos processos seletivos
        for (SolvedSelectionProcessTest ss : solvedSelectionProcessTestList) {
            allSolvedQuestions.addAll(ss.getSolvedQuestionList());
        }

        return allSolvedQuestions;
    }

    private PerformanceGeneralStatsDTO calculateGeneralStats(
            List<SolvedEvaluationTestQuestion> allSolvedQuestions,
            int numberOfQuestionnaires,
            int numberOfSelectionProcessTests
    ) {
        long totalResponseTimeInSeconds = 0L;
        long totalQuestions = 0L;
        long totalCorrect = 0L;

        for (SolvedEvaluationTestQuestion solved : allSolvedQuestions) {
            long diff = Duration.between(solved.getStartDateTime(), solved.getEndDateTime()).toSeconds();
            totalResponseTimeInSeconds += diff;
            totalQuestions++;
            if (Boolean.TRUE.equals(solved.getIsCorrect())) {
                totalCorrect++;
            }
        }

        double averageResponseTimeInMinutes = 0.0;
        if (totalQuestions > 0) {
            // totalResponseTimeInSeconds é a soma dos tempos de cada questão
            averageResponseTimeInMinutes = (double) totalResponseTimeInSeconds / totalQuestions / 60.0;
        }


        double overallCorrectPercentage = 0.0;
        if (totalQuestions > 0) {
            overallCorrectPercentage = (double) totalCorrect / totalQuestions * 100.0;
        }

        return new PerformanceGeneralStatsDTO(
                averageResponseTimeInMinutes,
                numberOfQuestionnaires,
                numberOfSelectionProcessTests,
                overallCorrectPercentage
        );
    }


    private List<MonthYearCountDTO> getQuestionsPerMonth(List<SolvedEvaluationTestQuestion> allSolvedQuestions) {
        Map<YearMonth, Long> groupedMap = allSolvedQuestions.stream()
                .collect(Collectors.groupingBy(
                        q -> YearMonth.from(q.getStartDateTime()),
                        Collectors.counting()
                ));

        List<MonthYearCountDTO> result = new ArrayList<>();
        for (Map.Entry<YearMonth, Long> entry : groupedMap.entrySet()) {
            result.add(new MonthYearCountDTO(
                    entry.getKey().getYear(),
                    entry.getKey().getMonthValue(),
                    entry.getValue()
            ));
        }
        return result;
    }


    private List<MonthYearCorrectIncorrectDTO> getCorrectIncorrectPerMonth(List<SolvedEvaluationTestQuestion> allSolvedQuestions) {
        Map<YearMonth, List<SolvedEvaluationTestQuestion>> groupedMap = allSolvedQuestions.stream()
                .collect(Collectors.groupingBy(q -> YearMonth.from(q.getStartDateTime())));

        List<MonthYearCorrectIncorrectDTO> result = new ArrayList<>();
        for (Map.Entry<YearMonth, List<SolvedEvaluationTestQuestion>> entry : groupedMap.entrySet()) {
            YearMonth ym = entry.getKey();
            List<SolvedEvaluationTestQuestion> questions = entry.getValue();

            long count = questions.size();
            long correctCount = questions.stream().filter(q -> Boolean.TRUE.equals(q.getIsCorrect())).count();
            long incorrectCount = count - correctCount;

            result.add(new MonthYearCorrectIncorrectDTO(
                    ym.getYear(),
                    ym.getMonthValue(),
                    correctCount,
                    incorrectCount
            ));
        }
        return result;
    }

    private List<SubjectPerformanceDTO> getSubjectPerformance(List<SolvedEvaluationTestQuestion> allSolvedQuestions) {
        // Agrupa por ID da disciplina
        Map<Long, List<SolvedEvaluationTestQuestion>> questionsBySubject = allSolvedQuestions.stream()
                .collect(Collectors.groupingBy(
                        q -> q.getQuestion().getSubject().getId()
                ));

        List<SubjectPerformanceDTO> subjectPerformanceList = new ArrayList<>();
        for (Map.Entry<Long, List<SolvedEvaluationTestQuestion>> entry : questionsBySubject.entrySet()) {
            Long subjectId = entry.getKey();
            List<SolvedEvaluationTestQuestion> subjectQuestions = entry.getValue();

            long subjectTotal = subjectQuestions.size();
            long subjectCorrect = subjectQuestions.stream().filter(q -> Boolean.TRUE.equals(q.getIsCorrect())).count();

            double subjectCorrectPercentage = 0.0;
            if (subjectTotal > 0) {
                subjectCorrectPercentage = (double) subjectCorrect / subjectTotal * 100.0;
            }

            // Supondo que o nome da disciplina venha de getSubject().getName()
            String subjectName = subjectQuestions.get(0).getQuestion().getSubject().getName();

            subjectPerformanceList.add(
                    new SubjectPerformanceDTO(
                            subjectId,
                            subjectName,
                            subjectCorrect,
                            subjectTotal,
                            subjectCorrectPercentage
                    )
            );
        }

        return subjectPerformanceList;
    }

    public Performance getPerformanceAiAnalysis(StudentPerformanceDTO studentPerformanceDTO) {
        return new Performance(studentPerformanceDTO, geminiService.getAiPerformanceAnalysis(studentPerformanceDTO));
    }
}
