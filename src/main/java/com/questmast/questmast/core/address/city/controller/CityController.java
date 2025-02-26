package com.questmast.questmast.core.address.city.controller;

import com.questmast.questmast.core.address.city.domain.dto.CityFormDTO;
import com.questmast.questmast.core.address.city.service.CityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/city")
@RequiredArgsConstructor
public class CityController {

    private final CityService cityService;

    @GetMapping("/extern/{uf}")
    public ResponseEntity<List<CityFormDTO>> listByFederateUnit(@PathVariable String uf) {
        return ResponseEntity.ok(cityService.getExternCitiesByUf(uf));
    }
}
