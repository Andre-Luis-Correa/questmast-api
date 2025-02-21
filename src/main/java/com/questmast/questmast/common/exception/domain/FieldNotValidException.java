package com.questmast.questmast.common.exception.domain;

import lombok.Getter;

@Getter
public class FieldNotValidException extends Exception {

    private String field;
    private String value;

    public FieldNotValidException(String field, String value) {
        super();
        this.field = field;
        this.value = value;
    }
}
