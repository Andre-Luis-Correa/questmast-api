package com.questmast.questmast.core.performance.domain.dto;

public record MonthYearCorrectIncorrectDTO(
        int year,
        int month,
        long correctCount,
        long incorrectCount
) {}
