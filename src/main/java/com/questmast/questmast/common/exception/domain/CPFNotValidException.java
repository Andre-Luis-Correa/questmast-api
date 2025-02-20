package com.questmast.questmast.common.exception.domain;

import lombok.Getter;

@Getter
public class CPFNotValidException extends Exception {

    private String cpf;

    public CPFNotValidException(String cpf) {
        super();
        this.cpf = cpf;
    }
}
