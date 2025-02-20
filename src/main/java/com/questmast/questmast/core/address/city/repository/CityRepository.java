package com.questmast.questmast.core.address.city.repository;

import com.questmast.questmast.core.address.city.domain.City;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    Optional<City> findByName(@NotBlank String name);
}
