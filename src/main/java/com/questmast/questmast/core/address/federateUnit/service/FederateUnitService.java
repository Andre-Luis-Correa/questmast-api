package com.questmast.questmast.core.address.federateUnit.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.federateUnit.domain.FederateUnit;
import com.questmast.questmast.core.address.federateUnit.repository.FederateUnitRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FederateUnitService {

    private final FederateUnitRepository federateUnitRepository;

    public FederateUnit getOrNullByAcronym(@NotBlank String acronym) {
        return federateUnitRepository.findByAcronym(acronym).orElse(null);
    }

    public FederateUnit findByAcronym(@NotBlank String acronym) {
        return federateUnitRepository.findByAcronym(acronym).orElseThrow(
                () -> new EntityNotFoundExcpetion("FederateUnit", "acronym", acronym)
        );
    }

    public FederateUnit findByName(String name) {
        return federateUnitRepository.findByName(name).orElseThrow(
                () -> new EntityNotFoundExcpetion("FederateUnit", "name", name)
        );
    }
}
