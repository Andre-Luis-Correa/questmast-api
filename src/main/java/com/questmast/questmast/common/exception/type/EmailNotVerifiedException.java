package com.questmast.questmast.common.exception.type;

import lombok.Getter;

@Getter
public class EmailNotVerifiedException extends RuntimeException {
    private String email;

    public EmailNotVerifiedException(String email){
        super();
        this.email = email;
    }
}
