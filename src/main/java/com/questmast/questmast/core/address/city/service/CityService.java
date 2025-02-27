package com.questmast.questmast.core.address.city.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.city.domain.dto.CityFormDTO;
import com.questmast.questmast.core.address.city.domain.model.City;
import com.questmast.questmast.core.address.city.repository.CityRepository;
import com.questmast.questmast.core.address.federateUnit.domain.FederateUnit;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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

    public List<CityFormDTO> getExternCitiesByUf(String uf) {
        String url = "https://servicodados.ibge.gov.br/api/v1/localidades/estados/"
                + uf + "/distritos";

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        List<Map<String, Object>> body = response.getBody();
        if (body == null) {
            return Collections.emptyList();
        }

        List<CityFormDTO> cityList = new ArrayList<>();
        for (Map<String, Object> distrito : body) {

            String cityName = (String) distrito.get("nome");

            Map<String, Object> municipio = (Map<String, Object>) distrito.get("municipio");
            if (municipio == null) continue;

            Map<String, Object> microrregiao = (Map<String, Object>) municipio.get("microrregiao");
            if (microrregiao == null) continue;

            Map<String, Object> mesorregiao = (Map<String, Object>) microrregiao.get("mesorregiao");
            if (mesorregiao == null) continue;

            Map<String, Object> ufMap = (Map<String, Object>) mesorregiao.get("UF");
            if (ufMap == null) continue;

            String ufNome = (String) ufMap.get("nome");

            CityFormDTO dto = new CityFormDTO(cityName, ufNome);
            cityList.add(dto);
        }

        return cityList.stream().sorted(Comparator.comparing(CityFormDTO::city)).toList();
    }

    public City getValidCity(CityFormDTO cityFormDTO, FederateUnit federateUnit) {
        City city = cityRepository.findByName(cityFormDTO.city()).orElse(null);

        if(city == null) {
            city = new City();
            city.setName(cityFormDTO.city());
            city.setFederateUnit(federateUnit);
        } else {
            return city;
        }

        return cityRepository.save(city);
    }
}
