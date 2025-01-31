package com.questmast.questmast.core.person;

import com.questmast.questmast.core.person.gender.Gender;
import com.questmast.questmast.core.person.cpf.CPF;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class NaturalPerson extends Person {

    @NotNull
    @Embedded
    private CPF cpf;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "gender_acronym", nullable = false)
    private Gender gender;

    @NotNull
    @Column(nullable = false)
    private Boolean isEmailVerified;
}
