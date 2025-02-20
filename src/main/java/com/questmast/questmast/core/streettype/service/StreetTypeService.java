package com.questmast.questmast.core.streettype.service;

import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.core.streettype.domain.StreetType;
import com.questmast.questmast.core.streettype.repository.StreetTypeRepository;
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
}
