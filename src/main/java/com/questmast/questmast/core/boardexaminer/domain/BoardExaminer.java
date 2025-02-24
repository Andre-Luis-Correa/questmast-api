package com.questmast.questmast.core.boardexaminer.domain;

import com.questmast.questmast.core.person.cnpj.CNPJ;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class BoardExaminer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Embedded
    private CNPJ cnpj;

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
