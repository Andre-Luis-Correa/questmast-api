package com.questmast.questmast.core.authentication.user.domain.dto;

import com.questmast.questmast.core.address.address.domain.dto.SpecificAddressFormDTO;
import com.questmast.questmast.core.contact.phone.domain.dto.PhoneFormDTO;
import com.questmast.questmast.core.enums.PersonRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record UserFormDTO(

        @NotBlank
        String password,

        @NotNull
        PersonRole personRole,

        @NotBlank
        String cpf,

        @NotBlank
        String genderAcronym,

        @NotBlank
        String name,

        @NotNull
        SpecificAddressFormDTO specificAddressFormDTO,

        @NotBlank
        String mainEmail,

        @NotBlank
        String recoveryEmail,

        @NotNull
        List<PhoneFormDTO> phoneList
) {
}
