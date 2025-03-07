package com.questmast.questmast.core.professionallevel.controller;

import com.questmast.questmast.core.professionallevel.domain.dto.ProfessionalLevelDTO;
import com.questmast.questmast.core.professionallevel.domain.dto.ProfessionalLevelFormDTO;
import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;
import com.questmast.questmast.core.professionallevel.service.ProfessionalLevelService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professional-level")
@RequiredArgsConstructor
public class ProfessionalLevelController {

    private final ProfessionalLevelService professionalLevelService;

    @PostMapping
    public ResponseEntity<ProfessionalLevelDTO> create(@RequestBody ProfessionalLevelFormDTO professionalLevelFormDTO) {
        ProfessionalLevel professionalLevel = professionalLevelService.save(professionalLevelFormDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(professionalLevelService.convertProfessionalLevelToDTO(professionalLevel));
    }

    @GetMapping
    public ResponseEntity<List<ProfessionalLevelDTO>> listAll() {
        List<ProfessionalLevel> professionalLevelList = professionalLevelService.findAll();
        return ResponseEntity.status(HttpStatus.OK)
                .body(professionalLevelService.convertToProfessionalLevelDTOList(professionalLevelList));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ProfessionalLevelDTO>> list(Pageable pageable) {
        Page<ProfessionalLevel> professionalLevelPage = professionalLevelService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(professionalLevelService.convertToProfessionalLevelDTOPage(professionalLevelPage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalLevelDTO> update(@PathVariable Long id, @RequestBody ProfessionalLevelFormDTO professionalLevelFormDTO) {
        ProfessionalLevel professionalLevel = professionalLevelService.findById(id);
        return ResponseEntity.status(HttpStatus.OK)
                .body(professionalLevelService.update(professionalLevel, professionalLevelFormDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalLevelDTO> findById(@PathVariable Long id) {
        ProfessionalLevel professionalLevel = professionalLevelService.findById(id);
        return ResponseEntity.ok(professionalLevelService.convertProfessionalLevelToDTO(professionalLevel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ProfessionalLevel professionalLevel = professionalLevelService.findById(id);
        professionalLevelService.delete(professionalLevel);
        return ResponseEntity.noContent().build();
    }
}