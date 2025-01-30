package com.questmast.questmast.endereco.TipoLogradouro;

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
public class TipoLogradouro {

    @Id
    private String sigla;

    @Column(nullable = false)
    private String nome;
}
