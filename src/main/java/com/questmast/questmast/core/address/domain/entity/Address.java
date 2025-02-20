package com.questmast.questmast.core.address.domain.entity;

import com.questmast.questmast.core.city.domain.City;
import com.questmast.questmast.core.street.domain.Street;
import com.questmast.questmast.core.neighborhood.domain.Neighborhood;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String cep;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "street_id", nullable = false)
    private Street street;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "neighborhood_id", nullable = false)
    private Neighborhood neighborhood;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "city_id", nullable = false)
    private City city;
}
