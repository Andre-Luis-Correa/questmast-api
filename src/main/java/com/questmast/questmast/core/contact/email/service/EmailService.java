package com.questmast.questmast.core.contact.email.service;

import com.questmast.questmast.common.exception.domain.InvalidContactException;
import com.questmast.questmast.core.contact.email.domain.dto.EmailFormDTO;
import com.questmast.questmast.core.contact.email.domain.entity.Email;
import com.questmast.questmast.core.contact.email.repository.EmailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final EmailRepository emailRepository;

    public List<Email> generateValidEmailList(List<EmailFormDTO> emailFormDTOS) {
        List<Email> emailList = new ArrayList<>();

        for(EmailFormDTO emailFormDTO : emailFormDTOS) {
            if(emailRepository.existsByEmail(emailFormDTO.email())) {
                throw new InvalidContactException("Email", emailFormDTO.email());
            }
            Email email = new Email(emailFormDTO.email());
            emailList.add(email);
        }

        return emailList;
    }
}
