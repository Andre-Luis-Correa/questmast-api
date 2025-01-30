package com.questmast.questmast.pessoa.sexo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Sexo {

    @Id
    private String sigla;

    @Column(nullable = false)
    private String descricao;
}
