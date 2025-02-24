package com.questmast.questmast.core.authentication.controller;

import com.questmast.questmast.common.exception.type.EmailNotVerifiedException;
import com.questmast.questmast.core.address.address.domain.entity.Address;
import com.questmast.questmast.core.address.address.service.AddressService;
import com.questmast.questmast.core.admin.service.AdminService;
import com.questmast.questmast.core.authentication.user.domain.dto.UserDTO;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.authentication.user.domain.dto.UserLoginDTO;
import com.questmast.questmast.core.authentication.user.domain.dto.UserResetPasswordDTO;
import com.questmast.questmast.core.authentication.user.domain.model.User;
import com.questmast.questmast.core.authentication.user.service.UserService;
import com.questmast.questmast.core.authentication.utils.UserDetailsServiceImpl;
import com.questmast.questmast.core.contact.email.EmailService;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.contact.phone.service.PhoneService;
import com.questmast.questmast.core.contentmoderator.service.ContentModeratorService;
import com.questmast.questmast.core.enums.PersonRole;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.gender.service.GenderService;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import com.questmast.questmast.core.person.cpf.service.CPFService;
import com.questmast.questmast.core.student.service.StudentService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/authentication")
public class AuthenticationController {

    private final UserDetailsServiceImpl userDetailsService;
    private final AdminService adminService;
    private final ContentModeratorService contentModeratorService;
    private final StudentService studentService;
    private final GenderService genderService;
    private final AddressService addressService;
    private final PhoneService phoneService;
    private final EmailService emailService;
    private final CPFService cpfService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<String> authenticateUser(@Valid @RequestBody UserLoginDTO userLoginDTO) {
        String token = userService.authenticateUser(userLoginDTO);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<Void> create(@Valid @RequestBody UserFormDTO userFormDTO) throws Exception {
        CPF cpf = cpfService.getValidCPF(userFormDTO.cpf());
        Gender gender = genderService.findByAcronym(userFormDTO.genderAcronym());
        Address address = addressService.create(userFormDTO.specificAddressFormDTO());
        List<Phone> phoneList = phoneService.generateValidPhoneList(userFormDTO.phoneList());

        if(userFormDTO.personRole().equals(PersonRole.ROLE_ADMIN)) {
            adminService.create(userFormDTO, cpf, gender, address, userFormDTO.mainEmail(), phoneList);
        } else if (userFormDTO.personRole().equals(PersonRole.ROLE_CONTENT_MODERATOR)) {
            contentModeratorService.create(userFormDTO, cpf, gender, address, userFormDTO.mainEmail(), phoneList);
        } else {
            studentService.create(userFormDTO, cpf, gender, address, userFormDTO.mainEmail(), phoneList);
        }

        userDetailsService.create(userFormDTO);

        emailService.sendRegistrationVerificationEmail(userFormDTO.mainEmail(), true);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/recovery-email/{email}")
    public ResponseEntity<Void> addRecoveryEmail(@PathVariable String email, @Valid @NotNull String recoveryEmail) {
        User user = userDetailsService.findByUsername(email);
        userDetailsService.updateUserRecoveryEmail(user, recoveryEmail);

        emailService.sendRegistrationVerificationEmail(recoveryEmail, false);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-main-email/{email}")
    public ResponseEntity<String> verifyMainEmail(@PathVariable String email) {
        User user = userDetailsService.findByUsername(email);
        userDetailsService.updateMainEmailVerificationStatus(user);

        return ResponseEntity.ok("Muito obrigado por confirmar seu email principal!");
    }

    @PostMapping("/verify-recovery-email/{email}")
    public ResponseEntity<String> verifyRecoveryMainEmail(@PathVariable String email) {
        User user = userDetailsService.findByRecoveryEmail(email);
        userDetailsService.updateRecoveryEmailVerificationStatus(user);

        return ResponseEntity.ok("Muito obrigado por confirmar seu email de recuperação!");
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDTO> getUserByEmail(@PathVariable String email) {
        User user = userDetailsService.findByUsernameOrRecoveryEmail(email);
        return ResponseEntity.ok(userDetailsService.convertUserToUserDTO(user));
    }

    @PostMapping("/password-change/{email}")
    public ResponseEntity<Void> requestPasswordChange(@PathVariable String email) {
        User user = userDetailsService.findByUsernameOrRecoveryEmail(email);

        if(user.getIsMainEmailVerified().equals(Boolean.FALSE) || (email.equals(user.getRecoveryEmail()) && user.getIsRecoveryEmailVerified().equals(Boolean.FALSE))) {
            throw new EmailNotVerifiedException(email);
        }

        String resetPasswordCode = userDetailsService.generateResetPasswordCode();
        userDetailsService.updateUserResetPasswordStatus(user, resetPasswordCode);

        emailService.sendResetPasswordCodeEmail(email, resetPasswordCode);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@Valid @RequestBody UserResetPasswordDTO userResetPasswordDTO) {
        User user = userDetailsService.findByResetPasswordCode(userResetPasswordDTO.resetPasswordCode());
        userDetailsService.validateResetPasswordCodeExpireDate(user.getResetPasswordCodeExpireDate());

        userDetailsService.updateUserPassword(user, userResetPasswordDTO);

        return ResponseEntity.ok().build();
    }
}