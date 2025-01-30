package com.questmast.questmast.contato.telefone;

import com.questmast.questmast.contato.ddd.DDD;
import com.questmast.questmast.contato.ddi.DDI;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Telefone {

    @Id
    private String numero;

    @ManyToOne
    @JoinColumn(name = "ddd_ddd", nullable = false)
    private DDD ddd;

    @ManyToOne
    @JoinColumn(name = "ddi_ddi", nullable = false)
    private DDI ddi;
}
