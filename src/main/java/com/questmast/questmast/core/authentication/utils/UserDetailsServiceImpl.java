package com.questmast.questmast.core.authentication.utils;

import com.questmast.questmast.common.exception.type.EmailNotVerifiedException;
import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.common.exception.type.ResetPasswordException;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.authentication.user.domain.dto.UserResetPasswordDTO;
import com.questmast.questmast.core.authentication.user.domain.model.User;
import com.questmast.questmast.core.authentication.user.repository.UserRepository;
import com.questmast.questmast.core.authentication.user.service.UserDetailsImpl;
import com.questmast.questmast.core.enums.PersonRole;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Log4j2
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: " + username);
        User user = findByUsername(username);
        if(user.getIsEmailVerified().equals(Boolean.FALSE)) throw new EmailNotVerifiedException();
        return new UserDetailsImpl(user);
    }

    public void create(PersonRole personRole, UserFormDTO userFormDTO) {
        String encodedPassword = new BCryptPasswordEncoder().encode(userFormDTO.password());
        User user = new User(userFormDTO.mainEmail(), userFormDTO.name(), userFormDTO.cpf(), encodedPassword, personRole, false);
        userRepository.save(user);
    }

    public User findByUsername(String username) {
        log.info("findByUsername: " + username);
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "email", username)
        );
    }

    public void updateEmailVerificationStatus(User user) {
        user.setIsEmailVerified(true);
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
}
