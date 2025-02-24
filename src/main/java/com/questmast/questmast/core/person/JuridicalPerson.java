package com.questmast.questmast.core.person;

import com.questmast.questmast.core.person.cnpj.CNPJ;
import jakarta.persistence.Embedded;
import jakarta.persistence.MappedSuperclass;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class JuridicalPerson extends Person {

    @NotNull
    @Embedded
    private CNPJ cnpj;
}
