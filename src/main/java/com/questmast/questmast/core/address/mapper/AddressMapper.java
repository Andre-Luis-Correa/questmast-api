package com.questmast.questmast.core.address.mapper;

import com.questmast.questmast.core.address.domain.entity.Address;
import com.questmast.questmast.core.city.domain.City;
import com.questmast.questmast.core.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.street.domain.Street;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface AddressMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "cep", target = "cep")
    @Mapping(source = "street", target = "street")
    @Mapping(source = "neighborhood", target = "neighborhood")
    @Mapping(source = "city", target = "city")
    Address convertToAddress(String cep, Street street, Neighborhood neighborhood, City city);
}
