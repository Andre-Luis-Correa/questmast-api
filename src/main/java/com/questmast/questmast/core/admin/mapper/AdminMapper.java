package com.questmast.questmast.core.admin.mapper;

import com.questmast.questmast.core.address.domain.entity.SpecificAddress;
import com.questmast.questmast.core.admin.domain.dto.AdminDTO;
import com.questmast.questmast.core.admin.domain.dto.AdminFormDTO;
import com.questmast.questmast.core.admin.domain.model.Admin;
import com.questmast.questmast.core.contact.email.domain.entity.Email;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.person.cpf.CPF;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AdminMapper {

    Admin convertToAdmin(AdminFormDTO adminFormDTO, CPF cpf, Gender gender, SpecificAddress specificAddress, List<Phone> phoneList, List<Email> emailList);

    AdminDTO convertAdminToAdminDTO(Admin admin);
}
