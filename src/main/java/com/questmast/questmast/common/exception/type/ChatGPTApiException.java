package com.questmast.questmast.common.exception.type;

import lombok.Data;

@Data
public class ChatGPTApiException extends RuntimeException{

    private String message;

    public ChatGPTApiException(String message) {
        super();
        this.message = message;
    }
}
