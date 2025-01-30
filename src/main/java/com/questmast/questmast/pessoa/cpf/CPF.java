package com.questmast.questmast.pessoa.cpf;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Embeddable
public class CPF {

    @Id
    private String cpf;
}
