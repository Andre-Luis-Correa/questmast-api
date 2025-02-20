package com.questmast.questmast.core.street.service;

import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.core.street.domain.Street;
import com.questmast.questmast.core.street.repository.StreetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StreetService {

    private final StreetRepository streetRepository;

    public Street findById(Long id) {
        return streetRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("Street", "id", id.toString())
        );
    }
}
