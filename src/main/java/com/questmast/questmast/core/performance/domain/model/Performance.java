package com.questmast.questmast.core.performance.domain.model;

import com.questmast.questmast.core.performance.domain.dto.StudentPerformanceDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Performance {

    private StudentPerformanceDTO studentPerformanceDTO;
    private String aiAnalysis;
}
