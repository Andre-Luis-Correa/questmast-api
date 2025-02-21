package com.questmast.questmast.core.gender.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.gender.repository.GenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;

    public Gender findByAcronym(String acronym) {
        return genderRepository.findByAcronym(acronym).orElseThrow(
                () -> new EntityNotFoundExcpetion("Gender", "acronym", acronym)
        );
    }

}
