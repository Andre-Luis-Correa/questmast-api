package com.questmast.questmast.core.person.cnpj;

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
public class CNPJ {

    @NotNull
    @Column(nullable = false, unique = true)
    private String cnpj;
}
