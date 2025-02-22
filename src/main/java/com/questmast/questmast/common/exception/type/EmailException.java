package com.questmast.questmast.common.exception.type;

import lombok.Getter;

@Getter
public class EmailException extends RuntimeException {
    private String email;

    public EmailException(String email) {
        super();
        this.email = email;
    }
}
