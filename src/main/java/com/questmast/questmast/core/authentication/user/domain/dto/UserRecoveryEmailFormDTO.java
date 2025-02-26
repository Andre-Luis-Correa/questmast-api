package com.questmast.questmast.core.authentication.user.domain.dto;

import com.questmast.questmast.core.enums.PersonRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRecoveryEmailFormDTO(
        @NotBlank
        String mainEmail,

        @NotNull
        PersonRole personRole,

        @NotBlank
        String recoveryEmail
) {
}
