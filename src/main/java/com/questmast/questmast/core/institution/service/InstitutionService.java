package com.questmast.questmast.core.institution.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.institution.domain.model.Institution;
import com.questmast.questmast.core.institution.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    public Institution findById(Long id) {
        return institutionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("Institution", "id", id.toString())
        );
    }
}
