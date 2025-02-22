package com.questmast.questmast.core.address.address.controller;

import com.questmast.questmast.common.exception.type.FieldNotValidException;
import com.questmast.questmast.core.address.address.domain.dto.SpecificAddressFormDTO;
import com.questmast.questmast.core.address.address.service.AddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/extern/{cep}")
    public ResponseEntity<SpecificAddressFormDTO> getExternAddress(@PathVariable String cep) throws Exception {
        return ResponseEntity.ok().body(addressService.getExternAddressByCep(cep));
    }
}
