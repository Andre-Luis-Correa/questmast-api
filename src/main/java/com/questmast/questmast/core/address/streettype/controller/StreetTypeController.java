package com.questmast.questmast.core.address.streettype.controller;

import com.questmast.questmast.core.address.streettype.domain.StreetType;
import com.questmast.questmast.core.address.streettype.service.StreetTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/street-type")
@RequiredArgsConstructor
public class StreetTypeController {

    private final StreetTypeService streetTypeService;

    public ResponseEntity<List<StreetType>> list() {
        return ResponseEntity.ok(streetTypeService.findAll());
    }
}
