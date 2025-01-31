package com.questmast.questmast.core.boardexaminer;

import com.questmast.questmast.core.person.JuridicalPerson;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(callSuper = true)
public class BoardExaminer extends JuridicalPerson {

    @NotNull
    @Column(nullable = false)
    private String siteUrl;

    @NotNull
    @Column(nullable = false)
    private Integer quantityOfTests;

    @NotNull
    @Column(nullable = false)
    private Integer quantityOfQuestions;

    @NotNull
    @Column(nullable = false)
    private Integer quantityOfSelectionProcess;
}
