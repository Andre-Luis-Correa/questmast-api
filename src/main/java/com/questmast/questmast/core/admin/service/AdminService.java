package com.questmast.questmast.core.admin.service;

import com.questmast.questmast.core.address.domain.entity.Address;
import com.questmast.questmast.core.address.domain.entity.SpecificAddress;
import com.questmast.questmast.core.admin.domain.dto.AdminDTO;
import com.questmast.questmast.core.admin.domain.dto.AdminFormDTO;
import com.questmast.questmast.core.admin.domain.model.Admin;
import com.questmast.questmast.core.admin.mapper.AdminMapper;
import com.questmast.questmast.core.admin.repository.AdminRepository;
import com.questmast.questmast.core.authentication.user.model.User;
import com.questmast.questmast.core.authentication.user.service.UserDetailsServiceImpl;
import com.questmast.questmast.core.contact.email.domain.entity.Email;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.enums.PersonRole;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.person.cpf.CPF;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminRepository adminRepository;
    private final AdminMapper adminMapper;
    private final UserDetailsServiceImpl userDetailsService;

    public AdminDTO create(AdminFormDTO adminFormDTO, Gender gender, Address address, List<Phone> phoneList, List<Email> emailList) {
        SpecificAddress specificAddress = new SpecificAddress(adminFormDTO.specificAddressFormDTO().number(), adminFormDTO.specificAddressFormDTO().complement(), address);
        CPF cpf = new CPF(adminFormDTO.cpf());
        Admin admin = convertToAdmin(adminFormDTO, cpf, gender, specificAddress, phoneList, emailList);
        admin.setIsEmailVerified(true);

        Admin adminSaved = adminRepository.save(admin);

        User user = new User(adminFormDTO.emailForLogin(), adminFormDTO.password(), PersonRole.ROLE_ADMIN);
        userDetailsService.createUser(user);

        return convertAdminToAdminDTO(adminSaved);
    }

    private AdminDTO convertAdminToAdminDTO(Admin admin) {
        return adminMapper.convertAdminToAdminDTO(admin);
    }

    private Admin convertToAdmin(AdminFormDTO adminFormDTO, CPF cpf, Gender gender, SpecificAddress specificAddress, List<Phone> phoneList, List<Email> emailList) {
        return adminMapper.convertToAdmin(adminFormDTO, cpf, gender, specificAddress, phoneList, emailList);
    }
}
