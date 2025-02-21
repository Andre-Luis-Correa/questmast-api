package com.questmast.questmast.core.solvedevaluationtest;

import com.questmast.questmast.core.solvedevaluationtestquestion.SolvedEvaluationTestQuestion;
import com.questmast.questmast.core.student.domain.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@MappedSuperclass
public class SolvedEvaluationTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @NotNull
    @Column(nullable = false)
    private Integer quantityOfCorrectAnswers;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @NotNull
    @OneToMany(cascade = CascadeType.ALL)
    private List<SolvedEvaluationTestQuestion> solvedQuestionList;
}
