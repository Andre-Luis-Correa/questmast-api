package com.questmast.questmast.core.address.address.mapper;

import com.questmast.questmast.core.address.address.domain.entity.Address;
import com.questmast.questmast.core.address.city.domain.City;
import com.questmast.questmast.core.address.neighborhood.domain.Neighborhood;
import com.questmast.questmast.core.address.street.domain.Street;
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
