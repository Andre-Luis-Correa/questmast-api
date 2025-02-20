package com.questmast.questmast.core.city.service;

import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.core.city.domain.City;
import com.questmast.questmast.core.city.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository cityRepository;

    public City findById(Long id) {
        return cityRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("City", "Id", id.toString())
        );
    }
}
