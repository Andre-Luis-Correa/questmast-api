package com.questmast.questmast.core.address.street.repository;

import com.questmast.questmast.core.address.street.domain.Street;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {

    Optional<Street> findByName(@NotBlank String name);
}
