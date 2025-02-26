package com.questmast.questmast.core.authentication.utils;

import com.questmast.questmast.common.exception.type.EmailNotVerifiedException;
import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.common.exception.type.ResetPasswordException;
import com.questmast.questmast.core.authentication.user.domain.dto.UserDTO;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.authentication.user.domain.dto.UserResetPasswordDTO;
import com.questmast.questmast.core.authentication.user.domain.model.User;
import com.questmast.questmast.core.authentication.user.mapper.UserMapper;
import com.questmast.questmast.core.authentication.user.repository.UserRepository;
import com.questmast.questmast.core.authentication.user.service.UserDetailsImpl;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: " + username);
        User user = findByUsername(username);
        if(user.getIsMainEmailVerified().equals(Boolean.FALSE)) throw new EmailNotVerifiedException(username);
        return new UserDetailsImpl(user);
    }

    public void create(UserFormDTO userFormDTO, String verificationCode) {
        String encodedPassword = new BCryptPasswordEncoder().encode(userFormDTO.password());
        User user = new User(userFormDTO.mainEmail(), userFormDTO.name(), userFormDTO.cpf(), encodedPassword, userFormDTO.personRole(), false, verificationCode);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "email", username)
        );
    }

    public User findByRecoveryEmail(String recoveryEmail) {
        return userRepository.findByRecoveryEmail(recoveryEmail).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "recoveryEmail", recoveryEmail)
        );
    }

    public void updateMainEmailVerificationStatus(User user) {
        user.setIsMainEmailVerified(true);
        user.setVerificationEmailCode(null);
        userRepository.save(user);
    }

    public void updateRecoveryEmailVerificationStatus(User user) {
        user.setIsRecoveryEmailVerified(true);
        user.setVerificationEmailCode(null);
        userRepository.save(user);
    }

    public String generateVerificationCode() {
        return UUID.randomUUID().toString();
    }

    public void updateUserResetPasswordStatus(User user, String resetPasswordCode) {
        user.setResetPasswordCode(resetPasswordCode);
        user.setResetPasswordCodeExpireDate(LocalDateTime.now().plusMinutes(20));
        userRepository.save(user);
    }

    public void updateUserPassword(User user, UserResetPasswordDTO userResetPasswordDTO) {
        user.setResetPasswordCode(null);
        user.setResetPasswordCodeExpireDate(null);
        user.setPassword(new BCryptPasswordEncoder().encode(userResetPasswordDTO.newPassword()));
        userRepository.save(user);
    }

    public User findByUsernameAndResetPasswordCode(String email, String resetPasswordCode) {
        return userRepository.findByUsernameAndResetPasswordCode(email, resetPasswordCode).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "username e resetPasswordCode", email +  " " + resetPasswordCode)
        );
    }

    public void validateResetPasswordCodeExpireDate(LocalDateTime resetPasswordCodeExpireDate) {
        if (resetPasswordCodeExpireDate == null || LocalDateTime.now().isAfter(resetPasswordCodeExpireDate)) {
            throw new ResetPasswordException("resetPasswordCodeExpireDate", "inválido");
        }
    }

    public User findByUsernameOrRecoveryEmail(String email) {
        return userRepository.findByUsernameOrRecoveryEmail(email).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "username ou recoveryEmail", email)
        );
    }

    public UserDTO convertUserToUserDTO(User user) {
        return userMapper.convertUserToUserDTO(user);
    }

    public void updateUserRecoveryEmail(User user, String recoveryEmail) {
        user.setRecoveryEmail(recoveryEmail);
        user.setIsRecoveryEmailVerified(false);
        userRepository.save(user);
    }

    public User findByResetPasswordCode(String resetPasswordCode) {
        return userRepository.findByResetPasswordCode(resetPasswordCode).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "resetPasswordCode", resetPasswordCode)
        );
    }

    public User findUserByCPF(String cpf) {
        return userRepository.findByCpf(new CPF(cpf)).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "cpf", cpf)
        );
    }

    public User getOrNullByUsernameAndVerificationEmailCode(String email, String verificationEmailCode) {
        return userRepository.findByUsernameAndVerificationEmailCode(email, verificationEmailCode).orElse(null);
    }

    public User findByRecoveryEmailAndVerificationEmailCode(String email, String verificationEmailCode) {
        return userRepository.findByRecoveryEmailAndVerificationEmailCode(email, verificationEmailCode).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "email e verificationEmailCode", email + " e " + verificationEmailCode)
        );
    }

    public void updateUserVerificationEmailCode(User user, String verificationEmailCode) {
        user.setVerificationEmailCode(verificationEmailCode);

        userRepository.save(user);
    }
}
