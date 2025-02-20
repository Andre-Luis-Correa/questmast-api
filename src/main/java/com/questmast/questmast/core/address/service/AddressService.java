package com.questmast.questmast.core.address.service;

import com.questmast.questmast.core.address.domain.dto.SpecificAddressFormDTO;
import com.questmast.questmast.core.address.domain.entity.Address;
import com.questmast.questmast.core.address.mapper.AddressMapper;
import com.questmast.questmast.core.address.repository.AddressRepository;
import com.questmast.questmast.core.city.domain.City;
import com.questmast.questmast.core.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.street.domain.Street;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepository addressRepository;
    private final AddressMapper addressMapper;

    public Address create(SpecificAddressFormDTO specificAddressFormDTO, Street street, Neighborhood neighborhood, City city) {
        Address address = convertToAddress(specificAddressFormDTO.cep(), street, neighborhood, city);
        return addressRepository.save(address);
    }

    private Address convertToAddress(String cep, Street street, Neighborhood neighborhood, City city) {
        return addressMapper.convertToAddress(cep, street, neighborhood, city);
    }
}
