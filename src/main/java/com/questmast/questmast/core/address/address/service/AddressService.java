package com.questmast.questmast.core.address.address.service;

import com.questmast.questmast.core.address.address.domain.dto.SpecificAddressFormDTO;
import com.questmast.questmast.core.address.address.domain.entity.Address;
import com.questmast.questmast.core.address.address.mapper.AddressMapper;
import com.questmast.questmast.core.address.address.repository.AddressRepository;
import com.questmast.questmast.core.address.city.domain.City;
import com.questmast.questmast.core.address.city.service.CityService;
import com.questmast.questmast.core.address.federateUnit.domain.FederateUnit;
import com.questmast.questmast.core.address.federateUnit.service.FederateUnitService;
import com.questmast.questmast.core.address.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.address.neighborhood.service.NeighborhoodService;
import com.questmast.questmast.core.address.street.domain.Street;
import com.questmast.questmast.core.address.street.service.StreetService;
import com.questmast.questmast.core.address.streettype.domain.StreetType;
import com.questmast.questmast.core.address.streettype.service.StreetTypeService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;
    private final StreetService streetService;
    private final StreetTypeService streetTypeService;
    private final NeighborhoodService neighborhoodService;
    private final CityService cityService;
    private final FederateUnitService federateUnitService;

    public Address create(@NotNull SpecificAddressFormDTO specificAddressFormDTO) {
        StreetType streetType = streetTypeService.findByAcronym(specificAddressFormDTO.streetType());
        FederateUnit federateUnit = federateUnitService.findByAcronym(specificAddressFormDTO.federateUnit());

        Street street = streetService.getOrNullByName(specificAddressFormDTO.street());
        if(street == null) {
            street = streetService.create(specificAddressFormDTO.street(), streetType);
        }

        Neighborhood neighborhood = neighborhoodService.getOrNullByName(specificAddressFormDTO.neighborhood());
        if(neighborhood == null) {
            neighborhood = neighborhoodService.create(specificAddressFormDTO.neighborhood());
        }

        City city = cityService.getOrNullByName(specificAddressFormDTO.city());
        if(city == null) {
            city = cityService.create(specificAddressFormDTO.city(), federateUnit);
        }

        Address address = convertToAddress(specificAddressFormDTO.cep(), street, neighborhood, city);
        return addressRepository.save(address);
    }

    private Address convertToAddress(String cep, Street street, Neighborhood neighborhood, City city) {
        return addressMapper.convertToAddress(cep, street, neighborhood, city);
    }
}
