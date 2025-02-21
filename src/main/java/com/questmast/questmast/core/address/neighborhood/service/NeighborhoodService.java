package com.questmast.questmast.core.address.neighborhood.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.address.neighborhood.repository.NeighborhoodRepository;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NeighborhoodService {

    private final NeighborhoodRepository neighborhoodRepository;

    public Neighborhood findById(Long id) {
        return neighborhoodRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("Neighborhood", "id", id.toString())
        );
    }

    public Neighborhood getOrNullByName(@NotBlank String name) {
        return neighborhoodRepository.findByName(name).orElse(null);
    }

    public Neighborhood create(@NotBlank String name) {
        Neighborhood neighborhood = new Neighborhood();
        neighborhood.setName(name);

        return neighborhoodRepository.save(neighborhood);
    }
}
