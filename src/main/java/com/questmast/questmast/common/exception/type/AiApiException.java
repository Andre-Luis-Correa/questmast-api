package com.questmast.questmast.common.exception.type;

import lombok.Data;

@Data
public class AiApiException extends RuntimeException{

    private String message;

    public AiApiException(String message) {
        super();
        this.message = message;
    }
}
