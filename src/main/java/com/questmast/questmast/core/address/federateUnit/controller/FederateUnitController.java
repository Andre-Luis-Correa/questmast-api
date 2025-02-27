package com.questmast.questmast.core.address.federateUnit.controller;

import com.questmast.questmast.core.address.federateUnit.domain.FederateUnit;
import com.questmast.questmast.core.address.federateUnit.service.FederateUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/federate-unit")
@RequiredArgsConstructor
public class FederateUnitController {

    private final FederateUnitService federateUnitService;

    @GetMapping
    public ResponseEntity<List<FederateUnit>> list() {
        return ResponseEntity.ok(federateUnitService.findAll());
    }
}
