package com.questmast.questmast.core.address.address.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.common.exception.type.FieldNotValidException;
import com.questmast.questmast.common.exception.type.HttpConnectionException;
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

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
    private static final String VIA_CEP_API = "http://viacep.com.br/ws/";

    public Address create(@NotNull SpecificAddressFormDTO specificAddressFormDTO) {
        StreetType streetType = streetTypeService.findByName(specificAddressFormDTO.streetType());
        FederateUnit federateUnit = federateUnitService.findByName(specificAddressFormDTO.federateUnit());

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

    public SpecificAddressFormDTO getExternAddressByCep(String cep) throws Exception {
        cep = cep.replace("-", "");

        if(cep.isBlank() && !cep.matches("\\d{8}")) {
            throw new FieldNotValidException("Cep", cep);
        }

        StringBuilder response = getResponseFromViaCep(cep);

        Map<String, String> jsonMap = convertViaCepJsonToMap(response.toString());

        if (jsonMap.containsKey("erro")) {
            throw new EntityNotFoundExcpetion("Address", "cep", cep);
        }

        return new SpecificAddressFormDTO(
                "",
                jsonMap.getOrDefault("complemento", ""),
                cep,
                jsonMap.getOrDefault("logradouro", ""),
                "",
                jsonMap.getOrDefault("bairro", ""),
                jsonMap.getOrDefault("localidade", ""),
                jsonMap.getOrDefault("estado", "")
        );
    }

    private static StringBuilder getResponseFromViaCep(String cep) throws Exception {
        String strUrl = VIA_CEP_API + cep + "/json";

        URL url = new URL(strUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod("GET");

        if (httpURLConnection.getResponseCode() != 200) {
            throw new HttpConnectionException(VIA_CEP_API);
        }

        StringBuilder jsonResponse = new StringBuilder();
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()))) {
            String linha;
            while ((linha = bufferedReader.readLine()) != null) {
                jsonResponse.append(linha);
            }
        }
        return jsonResponse;
    }

    private static Map<String, String> convertViaCepJsonToMap(String json) {
        Map<String, String> map = new HashMap<>();
        json = json.replaceAll("[{}\"]", "");
        String[] pares = json.split(",");

        for (String par : pares) {
            String[] chaveValor = par.split(":");
            if (chaveValor.length == 2) {
                map.put(chaveValor[0].trim(), chaveValor[1].trim());
            }
        }
        return map;
    }
}
