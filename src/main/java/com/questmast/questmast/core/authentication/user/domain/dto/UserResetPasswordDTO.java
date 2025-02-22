package com.questmast.questmast.core.authentication.user.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserResetPasswordDTO(

        @NotNull
        @NotBlank
        String newPassword,

        @NotNull
        @NotBlank
        String resetPasswordCode
) {
}
