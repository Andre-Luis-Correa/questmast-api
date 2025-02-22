package com.questmast.questmast.common.exception.type;

import lombok.Getter;

@Getter
public class ResetPasswordException extends RuntimeException {

    String field;
    String reason;

    public ResetPasswordException(String field, String reason) {
        super();
        this.field = field;
        this.reason = reason;
    }
}
