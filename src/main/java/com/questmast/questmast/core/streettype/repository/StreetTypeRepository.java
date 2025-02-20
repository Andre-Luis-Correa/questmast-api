package com.questmast.questmast.core.streettype.repository;

import com.questmast.questmast.core.streettype.domain.StreetType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public interface StreetTypeRepository extends JpaRepository<StreetType, String> {
    Optional<StreetType> findByAcronym(String acronym);
}
