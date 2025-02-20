package com.questmast.questmast.core.person.cpf.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CPF {

    @NotNull
    @Column(nullable = false, unique = true)
    private String cpf;
}
