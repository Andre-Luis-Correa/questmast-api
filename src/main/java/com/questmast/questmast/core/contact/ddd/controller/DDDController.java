package com.questmast.questmast.core.contact.ddd.controller;

import com.questmast.questmast.core.contact.ddd.domain.DDD;
import com.questmast.questmast.core.contact.ddd.service.DDDService;
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
@RequestMapping("/ddd")
public class DDDController {

    private final DDDService dddService;

    @GetMapping
    public ResponseEntity<List<DDD>> list() {
        return ResponseEntity.ok(dddService.findAll());
    }
}
