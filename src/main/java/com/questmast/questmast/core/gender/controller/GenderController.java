package com.questmast.questmast.core.gender.controller;

import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.gender.service.GenderService;
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
@RequestMapping("/gender")
public class GenderController {

    private final GenderService genderService;

    @GetMapping
    public ResponseEntity<List<Gender>> list() {
        return ResponseEntity.ok(genderService.findAll());
    }
}
