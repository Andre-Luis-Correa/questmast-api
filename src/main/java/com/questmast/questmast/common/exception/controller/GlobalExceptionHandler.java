package com.questmast.questmast.common.exception.controller;

import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.questmast.questmast.common.exception.config.ErrorDescription;
import com.questmast.questmast.common.exception.type.*;
import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

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

    @ExceptionHandler(GoogleCloudException.class)
    public ResponseEntity<ErrorDescription> handleGoogleCloudException(GoogleCloudException ex) {
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
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

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<ErrorDescription> handleNotAuthorizedException(NotAuthorizedException ex) {
        String message = "Usuário com email "+ ex.getEmail() + " não possui permissão para realizar a ação " + ex.getAction() + ".";
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                message
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorDescription> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex) {
        String parameterName = ex.getParameterName();
        String parameterType = ex.getParameterType();

        String message = String.format(
                "O parâmetro obrigatório '%s' do tipo '%s' está faltando na requisição.",
                parameterName, parameterType
        );

        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.BAD_REQUEST.value(),
                message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDescription> handleConstraintViolationException(ConstraintViolationException ex) {

        List<String> violationMessages = ex.getConstraintViolations().stream()
                .map(violation -> String.format(
                        "Campo '%s': %s (valor fornecido: '%s')",
                        violation.getPropertyPath(),
                        violation.getMessage(),
                        violation.getInvalidValue()
                ))
                .collect(Collectors.toList());

        String message = "Erro de validação: " + String.join("; ", violationMessages);

        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.BAD_REQUEST.value(),
                message
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    @ExceptionHandler(JwtTokenException.class)
    public ResponseEntity<ErrorDescription> handleJwtTokenException(JwtTokenException ex) {
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(JWTVerificationException.class)
    public ResponseEntity<ErrorDescription> handleJWTVerificationException(JWTVerificationException ex) {
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(PDFException.class)
    public ResponseEntity<ErrorDescription> handlePDFException(PDFException ex) {
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                "Não foi possível ler o PDF."
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(AiApiException.class)
    public ResponseEntity<ErrorDescription> handleChatGPTApiException(AiApiException ex) {
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                "Não foi possível conectar-se ao Chat GPT para " + ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(JWTCreationException.class)
    public ResponseEntity<ErrorDescription> handleJWTCreationException(JWTCreationException ex) {
        ErrorDescription errorResponse = new ErrorDescription(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(QuestionException.class)
    public ResponseEntity<ErrorDescription> handleQuestionException(QuestionException ex) {
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