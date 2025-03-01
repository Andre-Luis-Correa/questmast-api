package com.questmast.questmast.common.exception.type;

import lombok.Data;

@Data
public class GoogleCloudException extends RuntimeException{

    private String message;

    public GoogleCloudException(String message) {
        super();
        this.message = message;
    }
}
