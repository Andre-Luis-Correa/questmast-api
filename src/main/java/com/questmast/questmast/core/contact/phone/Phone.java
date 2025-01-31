package com.questmast.questmast.core.contact.phone;

import com.questmast.questmast.core.contact.ddd.DDD;
import com.questmast.questmast.core.contact.ddi.DDI;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Phone {

    @Id
    private String number;

    @ManyToOne
    @JoinColumn(name = "ddd_ddd", nullable = false)
    private DDD ddd;

    @ManyToOne
    @JoinColumn(name = "ddi_ddi", nullable = false)
    private DDI ddi;
}
