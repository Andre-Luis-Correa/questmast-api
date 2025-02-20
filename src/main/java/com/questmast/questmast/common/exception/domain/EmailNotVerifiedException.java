package com.questmast.questmast.common.exception.domain;

public class EmailNotVerifiedException extends RuntimeException {
    public EmailNotVerifiedException(){
        super("É necessário realizar a verificação do email.");
    }
}
