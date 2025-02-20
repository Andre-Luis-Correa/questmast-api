package com.questmast.questmast.common.exception.controller;

import com.questmast.questmast.common.exception.config.ErrorDescription;
import com.questmast.questmast.common.exception.domain.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundExcpetion.class)
    public ResponseEntity<ErrorDescription> handleEntityNotFoundException(EntityNotFoundExcpetion ex) {
        String message = ex.getEntity() + " não encontrada para " + ex.getField() + " " + ex.getValue() + ".";
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidContactException.class)
    public ResponseEntity<ErrorDescription> handleInvalidContactException(InvalidContactException ex) {
        String message = "O " + ex.getEntity() + " (" + ex.getValue() + ") já está sendo utilizado.";
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(CPFNotValidException.class)
    public ResponseEntity<ErrorDescription> handleCPFNotValidException(CPFNotValidException ex) {
        String message = "O CPF " + ex.getCpf() + " é inválido.";
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EmailNotValidException.class)
    public ResponseEntity<ErrorDescription> handleEmailNotValidException(EmailNotValidException ex) {
        String message = "O email " + ex.getCpf() + " é inválido.";
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RegistrationEmailException.class)
    public ResponseEntity<ErrorDescription> handleRegistrationEmailException(RegistrationEmailException ex) {
        String message = "Não foi possível enviar o email de verificação, o registro não foi concluído.";
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorDescription> handleEmailNotVerified(EmailNotVerifiedException ex) {
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.FORBIDDEN.value(),
                ex.getMessage()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDescription> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        ErrorDescription errorDescription = new ErrorDescription(
                HttpStatus.CONFLICT.value(),
                "Dados duplicados: " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDescription);
    }
}
