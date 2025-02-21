package com.questmast.questmast.core.address.city.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.city.domain.City;
import com.questmast.questmast.core.address.city.repository.CityRepository;
import com.questmast.questmast.core.address.federateUnit.domain.FederateUnit;
import jakarta.validation.constraints.NotBlank;
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

    public City getOrNullByName(@NotBlank String name) {
        return cityRepository.findByName(name).orElse(null);
    }

    public City create(@NotBlank String name, FederateUnit federateUnit) {
        City city = new City();
        city.setName(name);
        city.setFederateUnit(federateUnit);

        return cityRepository.save(city);
    }
}
