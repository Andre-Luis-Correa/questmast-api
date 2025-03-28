package com.questmast.questmast.core.person.cpf.service;

import br.com.caelum.stella.validation.CPFValidator;
import com.questmast.questmast.common.exception.type.FieldNotValidException;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Log4j2
@Service
@RequiredArgsConstructor
public class CPFService {

    public CPF getValidCPF(@NotBlank String cpf) throws FieldNotValidException {
        CPFValidator cpfValidator = new CPFValidator();
        try {
            cpfValidator.assertValid(cpf);
            String cleanedCpf = cpf.replaceAll("\\D", "");

            return new CPF(cleanedCpf);
        } catch (Exception e) {
            log.error("Invalid CPF: " + cpf, e.getMessage());
            throw new FieldNotValidException("CPF", cpf);
        }
    }
}
