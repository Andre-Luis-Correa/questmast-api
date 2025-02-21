package com.questmast.questmast.core.person;

import com.questmast.questmast.core.address.address.domain.entity.SpecificAddress;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
@MappedSuperclass
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Embedded
    private SpecificAddress specificAddress;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Phone> phoneList;

    @NotNull
    @Column(nullable = false, unique = true)
    private String mainEmail;

    @NotNull
    @Column(nullable = false, unique = true)
    private String recoveryEmail;
}
