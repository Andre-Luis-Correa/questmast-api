package com.questmast.questmast.endereco.UnidadeFederativa;

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
public class UnidadeFederativa {
    @Id
    private String sigla;

    @Column(nullable = false)
    private String nome;
}
