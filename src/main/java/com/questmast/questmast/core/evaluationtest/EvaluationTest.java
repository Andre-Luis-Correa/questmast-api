package com.questmast.questmast.core.evaluationtest;

import com.questmast.questmast.core.question.domain.model.Question;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@MappedSuperclass
public class EvaluationTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Integer viewCounter;

    @NotEmpty
    @OneToMany(cascade = CascadeType.ALL)
    private List<Question> questionList;
}
