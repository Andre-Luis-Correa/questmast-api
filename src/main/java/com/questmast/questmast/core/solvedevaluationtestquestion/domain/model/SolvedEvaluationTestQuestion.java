package com.questmast.questmast.core.solvedevaluationtestquestion.domain.model;

import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class SolvedEvaluationTestQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private Boolean isCorrect;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "question_alternative_id", nullable = false)
    private QuestionAlternative questionAlternative;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;
}
