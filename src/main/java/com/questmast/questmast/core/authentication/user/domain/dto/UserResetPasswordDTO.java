package com.questmast.questmast.core.authentication.user.domain.dto;

import jakarta.validation.constraints.NotBlank;

public record UserResetPasswordDTO(

        @NotBlank
        String newPassword,

        @NotBlank
        String resetPasswordCode
) {
}
