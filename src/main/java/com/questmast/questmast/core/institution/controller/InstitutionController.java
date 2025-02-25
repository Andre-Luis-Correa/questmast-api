package com.questmast.questmast.core.institution.controller;

import com.questmast.questmast.core.institution.domain.dto.InstitutionFormDTO;
import com.questmast.questmast.core.institution.domain.model.Institution;
import com.questmast.questmast.core.institution.service.InstitutionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/institution")
public class InstitutionController {

    private final InstitutionService institutionService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody InstitutionFormDTO institutionFormDTO) {
        institutionService.create(institutionFormDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody InstitutionFormDTO institutionFormDTO) {
        Institution institution = institutionService.findById(id);
        institutionService.update(institution, institutionFormDTO);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Institution institution = institutionService.findById(id);
        institutionService.delete(institution);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Institution> getById(@PathVariable Long id) {
        Institution institution = institutionService.findById(id);

        return ResponseEntity.ok().body(institution);
    }

    @GetMapping
    public ResponseEntity<List<Institution>> list() {
        List<Institution> institutionList = institutionService.findAll();

        return ResponseEntity.ok().body(institutionList);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Institution>> list(Pageable pageable) {
        Page<Institution> institutionList = institutionService.findAll(pageable);

        return ResponseEntity.ok().body(institutionList);
    }
}
