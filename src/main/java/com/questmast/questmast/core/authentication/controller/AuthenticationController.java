package com.questmast.questmast.core.authentication.controller;

import com.questmast.questmast.common.exception.type.EmailNotVerifiedException;
import com.questmast.questmast.common.exception.type.FieldNotValidException;
import com.questmast.questmast.core.address.address.domain.entity.Address;
import com.questmast.questmast.core.address.address.service.AddressService;
import com.questmast.questmast.core.admin.service.AdminService;
import com.questmast.questmast.core.authentication.user.domain.dto.*;
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
    public ResponseEntity<String> create(@Valid @RequestBody UserFormDTO userFormDTO) throws FieldNotValidException {
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

        String verificationEmailCode = userDetailsService.generateVerificationCode();
        userDetailsService.create(userFormDTO, cpf, verificationEmailCode);

        emailService.sendRegistrationVerificationEmail(userFormDTO.mainEmail(), verificationEmailCode);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário cadastrado com sucesso!");
    }

    @PostMapping("/recovery-email")
    public ResponseEntity<Void> addRecoveryEmail(@Valid @RequestBody UserRecoveryEmailFormDTO userRecoveryEmailFormDTO) {
        User user = userDetailsService.findByUsername(userRecoveryEmailFormDTO.mainEmail());
        userDetailsService.updateUserRecoveryEmail(user, userRecoveryEmailFormDTO.recoveryEmail());

        if(userRecoveryEmailFormDTO.personRole().equals(PersonRole.ROLE_ADMIN)) {
            adminService.updateRecoveryEmail(userRecoveryEmailFormDTO);
        } else if (userRecoveryEmailFormDTO.personRole().equals(PersonRole.ROLE_CONTENT_MODERATOR)) {
            contentModeratorService.updateRecoveryEmail(userRecoveryEmailFormDTO);
        } else {
            studentService.updateRecoveryEmail(userRecoveryEmailFormDTO);
        }

        String verificationEmailCode = userDetailsService.generateVerificationCode();
        userDetailsService.updateUserVerificationEmailCode(user, verificationEmailCode);

        emailService.sendRegistrationVerificationEmail(userRecoveryEmailFormDTO.recoveryEmail(), verificationEmailCode);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/verify-email/{email}")
    public ResponseEntity<String> verifyEmail(@PathVariable String email, @RequestParam String verificationEmailCode) {
        User user = userDetailsService.getOrNullByUsernameAndVerificationEmailCode(email, verificationEmailCode);
        if(user == null) {
            user = userDetailsService.findByRecoveryEmailAndVerificationEmailCode(email, verificationEmailCode);
            userDetailsService.updateRecoveryEmailVerificationStatus(user);
        } else {
            userDetailsService.updateMainEmailVerificationStatus(user);
        }

        return ResponseEntity.ok("Muito obrigado por confirmar o email " + email + ".");
    }

    @GetMapping("/{cpf}")
    public ResponseEntity<UserDTO> getUserByCPF(@PathVariable String cpf) {
        String cleanedCpf = cpf.replaceAll("\\D", "");
        User user = userDetailsService.findUserByCPF(cleanedCpf);

        return ResponseEntity.ok(userDetailsService.convertUserToUserDTO(user));
    }

    @PostMapping("/password-change/{email}")
    public ResponseEntity<Void> requestPasswordChange(@PathVariable String email) {
        User user = userDetailsService.findByUsernameOrRecoveryEmail(email);

        if(user.getIsMainEmailVerified().equals(Boolean.FALSE) || (email.equals(user.getRecoveryEmail()) && user.getIsRecoveryEmailVerified().equals(Boolean.FALSE))) {
            throw new EmailNotVerifiedException(email);
        }

        String resetPasswordCode = userDetailsService.generateVerificationCode();
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