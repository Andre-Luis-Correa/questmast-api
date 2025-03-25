package com.questmast.questmast.core.performance.domain.dto;

public record MonthYearCountDTO(
        int year,
        int month,
        String monthName,
        long count
) {}
