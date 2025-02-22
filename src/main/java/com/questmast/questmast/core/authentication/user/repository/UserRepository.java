package com.questmast.questmast.core.authentication.user.repository;

import com.questmast.questmast.core.authentication.user.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndResetPasswordCode(String email, String resetPasswordCode);
}
