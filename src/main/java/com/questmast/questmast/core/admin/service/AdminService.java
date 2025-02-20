package com.questmast.questmast.core.admin.service;

import com.questmast.questmast.core.address.address.domain.entity.Address;
import com.questmast.questmast.core.address.address.domain.entity.SpecificAddress;
import com.questmast.questmast.core.admin.domain.dto.AdminDTO;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.admin.domain.model.Admin;
import com.questmast.questmast.core.admin.mapper.AdminMapper;
import com.questmast.questmast.core.admin.repository.AdminRepository;
import com.questmast.questmast.core.contact.email.domain.entity.Email;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;

    public void create(UserFormDTO userFormDTO, Gender gender, Address address, Email mainEmail, Email recoveryEmail, List<Phone> phoneList) {
        SpecificAddress specificAddress = new SpecificAddress(userFormDTO.specificAddressFormDTO().number(), userFormDTO.specificAddressFormDTO().complement(), address);
        CPF cpf = new CPF(userFormDTO.cpf());
        Admin admin = convertToAdmin(userFormDTO, cpf, gender, specificAddress, mainEmail, recoveryEmail, phoneList);
        admin.setIsEmailVerified(true);

        Admin adminSaved = adminRepository.save(admin);
    }

    private AdminDTO convertAdminToAdminDTO(Admin admin) {
        return adminMapper.convertAdminToAdminDTO(admin);
    }

    private Admin convertToAdmin(UserFormDTO userFormDTO, CPF cpf, Gender gender, SpecificAddress specificAddress, Email mainEmail, Email recoveryEmail, List<Phone> phoneList) {
        return adminMapper.convertToAdmin(userFormDTO, cpf, gender, specificAddress, mainEmail, recoveryEmail, phoneList);
    }
}
