package com.questmast.questmast.core.address.federateUnit.repository;

import com.questmast.questmast.core.address.federateUnit.domain.FederateUnit;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FederateUnitRepository extends JpaRepository<FederateUnit, String> {

    Optional<FederateUnit> findByAcronym(@NotBlank String acronym);
}
