package com.questmast.questmast.common.exception.domain;

import lombok.Getter;

@Getter
public class JwtTokenException extends RuntimeException {

    private String message;

    public JwtTokenException(String message) {
        super(message);
        this.message = message;
    }
}
