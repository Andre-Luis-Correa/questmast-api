package com.questmast.questmast.core.admin.domain.dto;

import com.questmast.questmast.core.address.address.domain.entity.SpecificAddress;
import com.questmast.questmast.core.contact.email.domain.entity.Email;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import com.questmast.questmast.core.gender.domain.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AdminDTO(
        @NotNull
        Long id,

        @NotBlank
        String name,

        @NotNull
        SpecificAddress specificAddress,

        @NotNull
        CPF cpf,

        @NotNull
        Gender gender,

        @NotNull
        Boolean isEmailVerified,

        @NotNull
        List<Phone> phoneList,

        @NotNull
        List<Email> emailList
        ) {
}
