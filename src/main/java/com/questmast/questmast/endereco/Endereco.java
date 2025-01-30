package com.questmast.questmast.endereco;

import com.questmast.questmast.endereco.Cidade.Cidade;
import com.questmast.questmast.endereco.Logradouro.Logradouro;
import com.questmast.questmast.endereco.bairro.Bairro;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cep;

    @ManyToOne
    @JoinColumn(name = "logradouro_id", nullable = false)
    private Logradouro logradouro;

    @ManyToOne
    @JoinColumn(name = "bairro_id", nullable = false)
    private Bairro bairro;

    @ManyToOne
    @JoinColumn(name = "cidade_id", nullable = false)
    private Cidade cidade;
}
