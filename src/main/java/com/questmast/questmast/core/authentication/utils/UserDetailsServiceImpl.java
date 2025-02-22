package com.questmast.questmast.core.authentication.utils;

import com.questmast.questmast.common.exception.type.DuplicatedFieldValueException;
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

    public void create(UserFormDTO userFormDTO) {
        if(userFormDTO.mainEmail().equals(userFormDTO.recoveryEmail())) {
            throw new DuplicatedFieldValueException("mainEmail", "recoveryEmail", userFormDTO.mainEmail());
        }

        String encodedPassword = new BCryptPasswordEncoder().encode(userFormDTO.password());
        User user = new User(userFormDTO.mainEmail(), userFormDTO.name(), userFormDTO.cpf(), encodedPassword, userFormDTO.personRole(), false);
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
        userRepository.save(user);
    }

    public void updateRecoveryEmailVerificationStatus(User user) {
        user.setIsRecoveryEmailVerified(true);
        userRepository.save(user);
    }

    public String generateResetPasswordCode() {
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

    public User findByUsernameOrRecoveryEmailAndResetPasswordCode(String email, String resetPasswordCode) {
        return userRepository.findByUsernameOrRecoveryEmailAndResetPasswordCode(email, resetPasswordCode).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "email", email +  " " + resetPasswordCode)
        );
    }
}
