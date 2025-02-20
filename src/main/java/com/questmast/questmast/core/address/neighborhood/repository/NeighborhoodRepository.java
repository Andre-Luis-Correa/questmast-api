package com.questmast.questmast.core.address.neighborhood.repository;

import com.questmast.questmast.core.address.neighborhood.domain.Neighborhood;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {

    Optional<Neighborhood> findByName(@NotBlank String name);
}
