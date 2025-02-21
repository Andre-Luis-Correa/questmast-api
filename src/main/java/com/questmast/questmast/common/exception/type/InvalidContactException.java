package com.questmast.questmast.common.exception.type;

import lombok.Getter;

@Getter
public class InvalidContactException extends RuntimeException {
    String entity;
    String value;

    public InvalidContactException(String entity, String value) {
        super();
        this.entity = entity;
        this.value = value;
    }
}
