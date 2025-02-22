package com.questmast.questmast.core.contentmoderator.mapper;

import com.questmast.questmast.core.address.address.domain.entity.SpecificAddress;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ContentModeratorMapper {

    @Mapping(source = "cpf", target = "cpf")
    @Mapping(source = "gender", target = "gender")
    @Mapping(source = "specificAddress", target = "specificAddress")
    @Mapping(source = "phoneList", target = "phoneList")
    ContentModerator convertToContentModerator(UserFormDTO userFormDTO, CPF cpf, Gender gender, SpecificAddress specificAddress, String mainEmail, String recoveryEmail, List<Phone> phoneList);
}
