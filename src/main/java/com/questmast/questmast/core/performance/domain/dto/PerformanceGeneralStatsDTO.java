package com.questmast.questmast.core.performance.domain.dto;

public record PerformanceGeneralStatsDTO(
        double averageResponseTimeInSeconds,
        int numberOfQuestionnaires,
        int numberOfSelectionProcessTests,
        double overallCorrectPercentage
) {}

