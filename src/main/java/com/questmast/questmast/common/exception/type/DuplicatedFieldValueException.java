package com.questmast.questmast.common.exception.type;

import lombok.Getter;

@Getter
public class DuplicatedFieldValueException extends RuntimeException {

    private String mainField;
    private String secondaryField;
    private String value;

    public DuplicatedFieldValueException(String mainField, String secondaryField, String value) {
        this.mainField = mainField;
        this.secondaryField = secondaryField;
        this.value = value;
    }
}
