package com.questmast.questmast.core.performance.domain.dto;

public record SubjectPerformanceDTO(
        Long subjectId,
        String subjectName,
        long correctCount,
        long totalCount,
        double correctPercentage
) {}
