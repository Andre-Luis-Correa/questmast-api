package com.questmast.questmast.core.authentication.user.model;

import com.questmast.questmast.core.enums.PersonRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
@Table(name = "app_user")
public class User {

    @Id
    private String username;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PersonRole personRole;
}
