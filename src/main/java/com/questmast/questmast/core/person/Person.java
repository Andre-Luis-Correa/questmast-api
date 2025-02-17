package com.questmast.questmast.core.person;

import com.questmast.questmast.core.contact.email.Email;
import com.questmast.questmast.core.contact.phone.Phone;
import com.questmast.questmast.core.address.SpecificAddress;
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
    @Column(nullable = false)
    private SpecificAddress specificAddress;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Phone> phoneList;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Email> emailList;
}
