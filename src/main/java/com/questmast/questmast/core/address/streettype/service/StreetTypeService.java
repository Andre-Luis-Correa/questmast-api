package com.questmast.questmast.core.address.streettype.service;

import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.streettype.domain.StreetType;
import com.questmast.questmast.core.address.streettype.repository.StreetTypeRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreetTypeService {

    private final StreetTypeRepository streetTypeRepository;

    public StreetType findByAcronym(String acronym) {
        return streetTypeRepository.findByAcronym(acronym).orElseThrow(
                () -> new EntityNotFoundExcpetion("StreetType", "acronym", acronym)
        );
    }

    public StreetType getOrNullByAcronym(@NotBlank String acronym) {
        return streetTypeRepository.findByAcronym(acronym).orElse(null);
    }

    public StreetType create(@NotBlank String acronym) {
        StreetType streetType = new StreetType(acronym, acronym);
        return streetTypeRepository.save(streetType);
    }
}
