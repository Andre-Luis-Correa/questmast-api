package com.questmast.questmast.core.gender.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.gender.repository.GenderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenderService {

    private final GenderRepository genderRepository;

    public Gender findByAcronym(String acronym) {
        return genderRepository.findByAcronym(acronym).orElseThrow(
                () -> new EntityNotFoundExcpetion("Gender", "acronym", acronym)
        );
    }

    public List<Gender> findAll() {
        return genderRepository.findAll();
    }
}
