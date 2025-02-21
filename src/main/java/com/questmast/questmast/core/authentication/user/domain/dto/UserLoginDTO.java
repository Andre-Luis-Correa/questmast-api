package com.questmast.questmast.core.authentication.user.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record UserLoginDTO(

        @NotBlank
        String password,

        @NotBlank
        String mainEmail
) {
}
