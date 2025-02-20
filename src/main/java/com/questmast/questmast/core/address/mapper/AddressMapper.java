package com.questmast.questmast.core.address.mapper;

import com.questmast.questmast.core.address.domain.entity.Address;
import com.questmast.questmast.core.city.domain.City;
import com.questmast.questmast.core.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.street.domain.Street;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    Address convertToAddress(String cep, Street street, Neighborhood neighborhood, City city);
}
