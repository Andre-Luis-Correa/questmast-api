package com.questmast.questmast.common.exception.domain;

import lombok.Getter;

@Getter
public class EmailNotValidException extends Exception {

    private String cpf;

    public EmailNotValidException(String cpf) {
        super();
        this.cpf = cpf;
    }
}
