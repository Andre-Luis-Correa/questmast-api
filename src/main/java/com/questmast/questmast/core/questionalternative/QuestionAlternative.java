package com.questmast.questmast.core.questionalternative;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class QuestionAlternative {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String alternativeLetter;

    @NotNull
    @Column(nullable = false)
    private String statement;

    @NotNull
    @Column(nullable = false)
    private Boolean isCorrect;
}
