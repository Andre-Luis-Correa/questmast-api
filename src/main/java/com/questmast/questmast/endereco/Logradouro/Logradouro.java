package com.questmast.questmast.endereco.Logradouro;

import com.questmast.questmast.endereco.TipoLogradouro.TipoLogradouro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Logradouro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nome;

    @ManyToOne
    @JoinColumn(name = "tipo_logradouro_sigla", nullable = false)
    private TipoLogradouro tipoLogradouro;
}
