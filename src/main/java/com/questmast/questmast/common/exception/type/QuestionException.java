package com.questmast.questmast.common.exception.type;

import lombok.Data;

@Data
public class QuestionException extends RuntimeException {
    private String message;

    public QuestionException(String message) {
        super();
        this.message = message;
    }
}
