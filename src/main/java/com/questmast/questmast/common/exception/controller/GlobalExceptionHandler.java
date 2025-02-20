package com.questmast.questmast.common.exception.controller;

import com.questmast.questmast.common.exception.config.ErrorDescription;
import com.questmast.questmast.common.exception.domain.EmailNotVerifiedException;
import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.common.exception.domain.InvalidContactException;
import com.questmast.questmast.common.exception.domain.RegistrationEmailException;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Hidden
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundExcpetion.class)
    public ResponseEntity<ErrorDescription> handleEntityNotFoundException(EntityNotFoundExcpetion ex) {
        String message = ex.getEntity() + " not found for " + ex.getField() + " " + ex.getValue();
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(InvalidContactException.class)
    public ResponseEntity<ErrorDescription> handleInvalidContactException(InvalidContactException ex) {
        String message = "The " + ex.getEntity() + " (" + ex.getValue() + ") is already in use";
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(RegistrationEmailException.class)
    public ResponseEntity<ErrorDescription> handleRegistrationEmailException(RegistrationEmailException ex) {
        String message = "It was Not possible to send verification email, the registration has not been concluded";
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

    @ExceptionHandler(org.springframework.dao.DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDescription> handleDataIntegrityViolationException() {
        ErrorDescription errorDescription = new ErrorDescription(
                HttpStatus.CONFLICT.value(),
                "Dados duplicados: um ou mais campos únicos já estão em uso."
        );
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDescription);
    }

}
