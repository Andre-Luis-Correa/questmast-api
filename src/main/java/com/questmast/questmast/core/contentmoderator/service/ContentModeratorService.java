package com.questmast.questmast.core.contentmoderator.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.address.domain.entity.Address;
import com.questmast.questmast.core.address.address.domain.entity.SpecificAddress;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.contentmoderator.mapper.ContentModeratorMapper;
import com.questmast.questmast.core.contentmoderator.repository.ContentModeratorRepository;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentModeratorService {

    private final ContentModeratorRepository contentModeratorRepository;
    private final ContentModeratorMapper contentModeratorMapper;

    public void create(UserFormDTO userFormDTO, CPF cpf, Gender gender, Address address, String mainEmail, List<Phone> phoneList) {
        SpecificAddress specificAddress = new SpecificAddress(userFormDTO.specificAddressFormDTO().number(), userFormDTO.specificAddressFormDTO().complement(), address);
        ContentModerator contentModerator = convertToContentModerator(userFormDTO, cpf, gender, specificAddress, mainEmail, phoneList);

        contentModeratorRepository.save(contentModerator);
    }

    private ContentModerator convertToContentModerator(UserFormDTO userFormDTO, CPF cpf, Gender gender, SpecificAddress specificAddress, String mainEmail, List<Phone> phoneList) {
        return contentModeratorMapper.convertToContentModerator(userFormDTO, cpf, gender, specificAddress, mainEmail, phoneList);
    }

    public ContentModerator findByMainEmail(String email) {
        return contentModeratorRepository.findByMainEmail(email).orElseThrow(
                () -> new EntityNotFoundExcpetion("ContentModerator", "email", email)
        );
    }

    public ContentModerator findById(Long id) {
        return contentModeratorRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("ContentModerator", "id", id.toString())
        );
    }
}
