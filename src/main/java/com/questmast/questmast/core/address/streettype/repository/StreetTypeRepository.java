package com.questmast.questmast.core.address.streettype.repository;

import com.questmast.questmast.core.address.streettype.domain.StreetType;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface StreetTypeRepository extends JpaRepository<StreetType, String> {

    Optional<StreetType> findByAcronym(@NotBlank String name);
}
