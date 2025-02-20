package com.questmast.questmast.core.address.streettype.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class StreetType {

    @Id
    private String acronym;

    @NotNull
    @Column(nullable = false)
    private String name;
}
