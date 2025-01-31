package com.questmast.questmast.core.person.cnpj;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Embeddable
public class CNPJ {

    @NotNull
    @Column(nullable = false, unique = true)
    private String cnpj;
}
