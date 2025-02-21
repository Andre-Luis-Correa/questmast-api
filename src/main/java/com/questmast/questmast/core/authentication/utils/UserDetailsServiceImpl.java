package com.questmast.questmast.core.authentication.utils;

import com.questmast.questmast.common.exception.domain.EmailNotVerifiedException;
import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.authentication.user.domain.entity.User;
import com.questmast.questmast.core.authentication.user.repository.UserRepository;
import com.questmast.questmast.core.authentication.user.service.UserDetailsImpl;
import com.questmast.questmast.core.enums.PersonRole;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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
        User user = new User(userFormDTO.mainEmail(), userFormDTO.name(), userFormDTO.cpf(), userFormDTO.password(), personRole, false);
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
}
