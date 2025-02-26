package com.questmast.questmast.core.authentication.user.domain.dto;

public record UserDTO(

        String name,

        String mainEmail,

        String recoveryEmail
) {
}
