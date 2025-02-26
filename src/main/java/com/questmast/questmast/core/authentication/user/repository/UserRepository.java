package com.questmast.questmast.core.authentication.user.repository;

import com.questmast.questmast.core.authentication.user.domain.model.User;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameAndResetPasswordCode(String email, String resetPasswordCode);

    @Query("SELECT us FROM User us WHERE us.username = :email OR us.recoveryEmail = :email")
    Optional<User> findByUsernameOrRecoveryEmail(String email);

    Optional<User> findByRecoveryEmail(String recoveryEmail);

    Optional<User> findByResetPasswordCode(String resetPasswordCode);

    Optional<User> findByCpf(CPF cpf);

    Optional<User> findByUsernameAndVerificationEmailCode(String email, String verificationEmailCode);

    Optional<User> findByRecoveryEmailAndVerificationEmailCode(String email, String verificationEmailCode);
}
