package com.questmast.questmast.core.contact.ddi.controller;

import com.questmast.questmast.core.contact.ddi.domain.DDI;
import com.questmast.questmast.core.contact.ddi.service.DDIService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/ddi")
public class DDIController {

    private final DDIService ddiService;

    @GetMapping
    public ResponseEntity<List<DDI>> list() {
        return ResponseEntity.ok(ddiService.findAll());
    }
}
