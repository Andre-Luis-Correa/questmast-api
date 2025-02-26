package com.questmast.questmast.common.exception.type;

import lombok.Getter;

@Getter
public class NotAuthorizedException extends RuntimeException {
    private String email;
    private String action;

    public NotAuthorizedException(String email, String action) {
        super();
        this.email = email;
        this.action = action;
    }
}
