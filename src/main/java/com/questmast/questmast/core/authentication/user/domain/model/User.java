package com.questmast.questmast.core.authentication.user.domain.model;

import com.questmast.questmast.core.enums.PersonRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    private String username;

    @Column(unique = true)
    private String recoveryEmail;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private String cpf;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonRole personRole;

    @NotNull
    @Column(nullable = false)
    private Boolean isMainEmailVerified;

    private Boolean isRecoveryEmailVerified;

    private String resetPasswordCode;

    private LocalDateTime resetPasswordCodeExpireDate;

    public User(String username, String name, String cpf, String password, PersonRole personRole, Boolean isMainEmailVerified) {
        this.username = username;
        this.name = name;
        this.cpf = cpf;
        this.password = password;
        this.personRole = personRole;
        this.isMainEmailVerified = isMainEmailVerified;
    }
}
