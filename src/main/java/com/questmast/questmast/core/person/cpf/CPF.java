package com.questmast.questmast.core.person.cpf;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
public class CPF {

    @NotNull
    @Column(nullable = false, unique = true)
    private String cpf;
}
