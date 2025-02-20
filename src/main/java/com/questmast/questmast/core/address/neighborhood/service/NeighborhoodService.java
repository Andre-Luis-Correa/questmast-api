package com.questmast.questmast.core.address.neighborhood.service;

import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.address.neighborhood.repository.NeighborhoodRepository;
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
}
