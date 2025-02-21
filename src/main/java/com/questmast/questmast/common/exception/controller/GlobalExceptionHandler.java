package com.questmast.questmast.common.exception.controller;

import com.questmast.questmast.common.exception.config.ErrorDescription;
import com.questmast.questmast.common.exception.type.*;
import io.swagger.v3.oas.annotations.Hidden;
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

        String message = "Violação de integridade de dados: " + ex.getMessage();
        // Mensagem padrão caso não consigamos tratar detalhadamente

        if (ex.getRootCause() instanceof org.postgresql.util.PSQLException) {
            org.postgresql.util.PSQLException psqlException = (org.postgresql.util.PSQLException) ex.getRootCause();

            // Verifica se é erro de unicidade (SQLState 23505)
            if ("23505".equals(psqlException.getSQLState())) {
                // Captura o detalhe da mensagem, que deve conter algo como:
                // "Key (campo)=(valor) already exists."
                String detail = psqlException.getServerErrorMessage().getDetail();

                if (detail != null) {
                    // Usamos expressão regular para isolar o nome do campo e o valor duplicado
                    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(".*\\((.*?)\\)=\\((.*?)\\).*");
                    java.util.regex.Matcher matcher = pattern.matcher(detail);

                    if (matcher.find()) {
                        // group(1) -> nome do campo
                        // group(2) -> valor duplicado
                        String field = matcher.group(1);
                        String value = matcher.group(2);

                        message = String.format("O campo '%s' com valor '%s' já está cadastrado.",
                                field, value);
                    } else {
                        // Caso o detalhe não siga o formato esperado,
                        // podemos usar uma mensagem genérica de duplicidade
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