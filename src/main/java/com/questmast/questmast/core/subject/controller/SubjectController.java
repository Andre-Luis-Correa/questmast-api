package com.questmast.questmast.core.subject.controller;

import com.questmast.questmast.core.subject.domain.dto.SubjectDTO;
import com.questmast.questmast.core.subject.domain.dto.SubjectFormDTO;
import com.questmast.questmast.core.subject.domain.entity.Subject;
import com.questmast.questmast.core.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectDTO> create(@RequestBody SubjectFormDTO subjectFormDTO) {
        Subject subject = subjectService.save(subjectFormDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(subjectService.convertSubjectToSubjectDetailsDTO(subject));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubjectDTO>> listAll() {
        List<Subject> subjectList = subjectService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.convertToSubjectDetailsDTOList(subjectList));
    }

    @GetMapping
    public ResponseEntity<Page<SubjectDTO>> list(Pageable pageable) {
        Page<Subject> subjectPage = subjectService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.convertToSubjectDetailsDTOPage(subjectPage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectDTO> update(@PathVariable Long id, @RequestBody SubjectFormDTO subjectFormDTO) {
        Subject subject = subjectService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(subjectService.update(subject, subjectFormDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectDTO> findById(@PathVariable Long id) {
        Subject subject = subjectService.findById(id);
        return ResponseEntity.ok(subjectService.convertSubjectToSubjectDetailsDTO(subject));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Subject subject = subjectService.findById(id);
        subjectService.delete(subject);
        return ResponseEntity.noContent().build();
    }
}