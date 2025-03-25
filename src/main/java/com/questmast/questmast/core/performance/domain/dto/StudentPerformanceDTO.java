package com.questmast.questmast.core.performance.domain.dto;

import java.util.List;

public record StudentPerformanceDTO(

        double averageResponseTimeInSeconds,
        int numberOfQuestionnaires,
        int numberOfSelectionProcessTests,
        double overallCorrectPercentage,

        List<MonthYearCountDTO> questionsPerMonth,
        List<MonthYearCorrectIncorrectDTO> correctIncorrectPerMonth,

        List<SubjectPerformanceDTO> topSubjects,
        List<SubjectPerformanceDTO> bottomSubjects

) {
}
