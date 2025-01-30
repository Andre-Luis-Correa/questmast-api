package com.questmast.questmast.pessoa;

import com.questmast.questmast.pessoa.cpf.CPF;
import com.questmast.questmast.pessoa.sexo.Sexo;
import jakarta.persistence.Embedded;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@MappedSuperclass
@EqualsAndHashCode(callSuper = true)
public class PessoaFisica extends Pessoa {

    @Embedded
    private CPF cpf;

    @ManyToOne
    @JoinColumn(name = "sexo_sigla", nullable = false)
    private Sexo sexo;
}
