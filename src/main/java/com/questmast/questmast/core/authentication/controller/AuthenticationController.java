package com.questmast.questmast.core.authentication.controller;

import com.questmast.questmast.core.address.address.domain.entity.Address;
import com.questmast.questmast.core.address.address.service.AddressService;
import com.questmast.questmast.core.admin.domain.dto.AdminDTO;
import com.questmast.questmast.core.admin.domain.model.Admin;
import com.questmast.questmast.core.admin.service.AdminService;
import com.questmast.questmast.core.authentication.service.AuthenticationService;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.authentication.user.domain.entity.User;
import com.questmast.questmast.core.authentication.user.service.UserDetailsServiceImpl;
import com.questmast.questmast.core.contact.email.EmailService;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.contact.phone.service.PhoneService;
import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.contentmoderator.service.ContentModeratorService;
import com.questmast.questmast.core.enums.PersonRole;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.gender.service.GenderService;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import com.questmast.questmast.core.person.cpf.service.CPFService;
import com.questmast.questmast.core.student.domain.Student;
import com.questmast.questmast.core.student.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/authentication")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UserDetailsServiceImpl userDetailsService;
    private final AdminService adminService;
    private final ContentModeratorService contentModeratorService;
    private final StudentService studentService;
    private final GenderService genderService;
    private final AddressService addressService;
    private final PhoneService phoneService;
    private final EmailService emailService;
    private final CPFService cpfService;

    @PostMapping
    public String authenticate(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }

    @PostMapping("/register/student")
    public ResponseEntity<AdminDTO> createStudent(@RequestBody UserFormDTO userFormDTO) throws Exception {
        CPF cpf = cpfService.getValidCPF(userFormDTO.cpf());
        Gender gender = genderService.findByAcronym(userFormDTO.genderAcronym());
        Address address = addressService.create(userFormDTO.specificAddressFormDTO());
        String mainEmail = emailService.getValidEmail(userFormDTO.mainEmail());
        String recoveryEmail = emailService.getValidEmail(userFormDTO.recoveryEmail());
        List<Phone> phoneList = phoneService.generateValidPhoneList(userFormDTO.phoneList());

        studentService.create(userFormDTO, cpf, gender, address, mainEmail, recoveryEmail, phoneList);
        userDetailsService.create(PersonRole.ROLE_STUDENT, userFormDTO);

        emailService.sendRegistrationVerificationEmail(PersonRole.ROLE_STUDENT, userFormDTO.mainEmail());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register/admin")
    public ResponseEntity<AdminDTO> createAdmin(@RequestBody UserFormDTO userFormDTO) throws Exception {
        CPF cpf = cpfService.getValidCPF(userFormDTO.cpf());
        Gender gender = genderService.findByAcronym(userFormDTO.genderAcronym());
        Address address = addressService.create(userFormDTO.specificAddressFormDTO());
        String mainEmail = emailService.getValidEmail(userFormDTO.mainEmail());
        String recoveryEmail = emailService.getValidEmail(userFormDTO.recoveryEmail());
        List<Phone> phoneList = phoneService.generateValidPhoneList(userFormDTO.phoneList());

        adminService.create(userFormDTO, cpf, gender, address, mainEmail, recoveryEmail, phoneList);

        userDetailsService.create(PersonRole.ROLE_ADMIN, userFormDTO);

        emailService.sendRegistrationVerificationEmail(PersonRole.ROLE_ADMIN, userFormDTO.mainEmail());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register/content-moderator")
    public ResponseEntity<AdminDTO> create(@RequestBody UserFormDTO userFormDTO) throws Exception {
        CPF cpf = cpfService.getValidCPF(userFormDTO.cpf());
        Gender gender = genderService.findByAcronym(userFormDTO.genderAcronym());
        Address address = addressService.create(userFormDTO.specificAddressFormDTO());
        String mainEmail = emailService.getValidEmail(userFormDTO.mainEmail());
        String recoveryEmail = emailService.getValidEmail(userFormDTO.recoveryEmail());
        List<Phone> phoneList = phoneService.generateValidPhoneList(userFormDTO.phoneList());

        contentModeratorService.create(userFormDTO, cpf, gender, address, mainEmail, recoveryEmail, phoneList);

        userDetailsService.create(PersonRole.ROLE_CONTENT_MODERATOR, userFormDTO);

        emailService.sendRegistrationVerificationEmail(PersonRole.ROLE_ADMIN, userFormDTO.mainEmail());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/verify-email/student/{email}")
    public ResponseEntity<String> verifyStudentEmail(@PathVariable String email) {
        Student student = studentService.findByMainEmail(email);
        studentService.updateEmailVerificationStatus(student);

        User user = userDetailsService.findByUsername(email);
        userDetailsService.updateEmailVerificationStatus(user);

        return ResponseEntity.ok("Muito obrigado por confirmar seu cadastro, " + student.getName()+ "!");
    }

    @PostMapping("/verify-email/admin/{email}")
    public ResponseEntity<String> verifyAdminEmail(@PathVariable String email) {
        Admin admin = adminService.findByMainEmail(email);
        adminService.updateEmailVerificationStatus(admin);

        return ResponseEntity.ok("Muito obrigado por confirmar seu cadastro, " + admin.getName()+ "!");
    }

    @PostMapping("/verify-email/content-moderator/{email}")
    public ResponseEntity<String> verifyContentModeratorEmail(@PathVariable String email) {
        ContentModerator contentModerator = contentModeratorService.findByMainEmail(email);
        contentModeratorService.updateEmailVerificationStatus(contentModerator);

        return ResponseEntity.ok("Muito obrigado por confirmar seu cadastro, " + contentModerator.getName()+ "!");
    }
}
