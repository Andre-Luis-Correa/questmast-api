package com.questmast.questmast.pessoa;

import com.questmast.questmast.contato.email.Email;
import com.questmast.questmast.contato.telefone.Telefone;
import com.questmast.questmast.endereco.EnderecoEspecifico;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@MappedSuperclass
public abstract class Pessoa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Embedded
    private EnderecoEspecifico enderecoEspecifico;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Telefone> telefoneList;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Email> emailList;
}
