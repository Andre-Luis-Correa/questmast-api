package com.questmast.questmast.core.admin.domain.dto;

import com.questmast.questmast.core.address.domain.dto.SpecificAddressFormDTO;
import com.questmast.questmast.core.contact.email.domain.dto.EmailFormDTO;
import com.questmast.questmast.core.contact.phone.domain.dto.PhoneFormDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record AdminFormDTO(

        @NotBlank
        String password,

        @NotBlank
        String emailForLogin,

        @NotBlank
        String cpf,

        @NotBlank
        String genderAcronym,

        @NotBlank
        String name,

        @NotNull
        SpecificAddressFormDTO specificAddressFormDTO,

        @NotNull
        List<PhoneFormDTO> phoneList,

        @NotNull
        List<EmailFormDTO> emailList
) {
}
