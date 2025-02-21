package com.questmast.questmast.core.authentication.user.service;

import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.core.authentication.user.authenticator.UserAuthenticated;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.authentication.user.domain.entity.User;
import com.questmast.questmast.core.authentication.user.repository.UserRepository;
import com.questmast.questmast.core.enums.PersonRole;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .map(UserAuthenticated::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                () -> new EntityNotFoundExcpetion("User", "email", username)
        );
    }

    public void create(PersonRole personRole, UserFormDTO userFormDTO) {
        User user = new User(userFormDTO.mainEmail(), userFormDTO.password(), personRole, false);
        userRepository.save(user);
    }

    public void updateEmailVerificationStatus(User user) {
        user.setIsEmailVerified(Boolean.TRUE);
        userRepository.save(user);
    }
}
