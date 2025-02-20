package com.questmast.questmast.core.authentication.user.service;

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

    public void create(UserFormDTO userFormDTO) {
        User user = new User(userFormDTO.emailList().get(0).email(), userFormDTO.password(), PersonRole.ROLE_ADMIN);
        userRepository.save(user);
    }
}
