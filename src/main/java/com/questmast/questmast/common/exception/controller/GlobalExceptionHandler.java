package com.questmast.questmast.common.exception.controller;

import com.questmast.questmast.common.exception.config.ErrorDescription;
import com.questmast.questmast.common.exception.type.*;
import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Iterator;
import java.util.List;

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
        String message = ex.getEntity() + " (" + ex.getValue() + ") já está sendo utilizado.";
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(FieldNotValidException.class)
    public ResponseEntity<ErrorDescription> handleEmailNotValidException(FieldNotValidException ex) {
        String message = "O " + ex.getField() + " " + ex.getValue() + " é inválido.";
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ErrorDescription> handleJwtTokenException(JwtTokenException ex) {
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(EmailNotVerifiedException.class)
    public ResponseEntity<ErrorDescription> handleEmailNotVerifiedException(EmailNotVerifiedException ex) {
        String message = "É necessário realizar a verificação do email " + ex.getEmail();
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.FORBIDDEN.value(),
                message
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(HttpConnectionException.class)
    public ResponseEntity<ErrorDescription> handleHttpConnectionException(HttpConnectionException ex) {
        String message = "Não foi possível se conectar ao web serviço " + ex.getWebService();
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.FORBIDDEN.value(),
                message
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(EmailException.class)
    public ResponseEntity<ErrorDescription> handleEmailException(EmailException ex) {
        String message = "Não foi possível enviar o email para " + ex.getEmail();
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.FORBIDDEN.value(),
                message
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(ResetPasswordException.class)
    public ResponseEntity<ErrorDescription> handleResetPasswordException(ResetPasswordException ex) {
        String message = "Não é possível realizar a alteração da senha, o campo " + ex.getField() + " é " + ex.getReason();
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.FORBIDDEN.value(),
                message
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(DuplicatedFieldValueException.class)
    public ResponseEntity<ErrorDescription> handleDuplicatedFieldValueException(DuplicatedFieldValueException ex) {
        String message = "Os campos " + ex.getMainField() + " e " + ex.getSecondaryField() + " não podem ter o mesmo valor " + ex.getValue();
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.FORBIDDEN.value(),
                message
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDescription> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        StringBuilder sb = new StringBuilder("Erro(s) de validação nos campos: ");

        for (int i = 0; i < fieldErrors.size(); i++) {
            FieldError fieldError = fieldErrors.get(i);

            sb.append("'").append(fieldError.getField()).append("'");

            sb.append(" (").append(fieldError.getDefaultMessage()).append(")");

            if (i < fieldErrors.size() - 1) {
                sb.append("; ");
            }
        }

        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.BAD_REQUEST.value(),
                sb.toString()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }


    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorDescription> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {

        String message = "Violação de integridade de dados: " + ex.getMessage();

        if (ex.getRootCause() instanceof org.postgresql.util.PSQLException) {
            org.postgresql.util.PSQLException psqlException = (org.postgresql.util.PSQLException) ex.getRootCause();

            if ("23505".equals(psqlException.getSQLState())) {
                String detail = psqlException.getServerErrorMessage().getDetail();

                if (detail != null) {
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(".*\\((.*?)\\)=\\((.*?)\\).*");
                    java.util.regex.Matcher matcher = pattern.matcher(detail);

                    if (matcher.find()) {
                        String field = matcher.group(1);
                        String value = matcher.group(2);

                        message = String.format("O campo '%s' com valor '%s' já está cadastrado.",
                                field, value);
                    } else {
                        message = "Registro duplicado: o valor informado já está cadastrado.";
                    }
                }
            }
        }

        ErrorDescription errorDescription = new ErrorDescription(
                HttpStatus.CONFLICT.value(),
                message
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorDescription);
    }
}