package com.questmast.questmast.core.address.street.service;

import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.street.domain.Street;
import com.questmast.questmast.core.address.street.repository.StreetRepository;
import com.questmast.questmast.core.address.streettype.domain.StreetType;
import jakarta.validation.constraints.NotBlank;
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

    public Street getOrNullByName(@NotBlank String name) {
        return streetRepository.findByName(name).orElse(null);
    }

    public Street create(@NotBlank String name, StreetType streetType) {
        Street street = new Street();
        street.setName(name);
        street.setStreetType(streetType);

        return streetRepository.save(street);
    }
}
