package com.questmast.questmast.core.authentication.user.domain.dto;

import jakarta.validation.constraints.NotNull;

public record UserDTO(

        @NotNull
        String name,

        @NotNull
        String mainEmail,

        @NotNull
        String recoveryEmail
) {
}
