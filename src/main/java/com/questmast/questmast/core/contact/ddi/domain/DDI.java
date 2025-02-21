package com.questmast.questmast.core.contact.ddi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class DDI {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer ddi;
}
