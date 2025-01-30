package com.questmast.questmast.endereco.Cidade;

import com.questmast.questmast.endereco.UnidadeFederativa.UnidadeFederativa;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Cidade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "unidade_federativa_id", nullable = false)
    private UnidadeFederativa unidadeFederativa;
}
