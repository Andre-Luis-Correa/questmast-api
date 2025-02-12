package com.questmast.questmast.core.subjecttopic.controller;

import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicDTO;
import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicFormDTO;
import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import com.questmast.questmast.core.subjecttopic.service.SubjectTopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subject-topic")
@RequiredArgsConstructor
public class SubjectTopicController {

    private final SubjectTopicService subjectTopicService;

    @PostMapping
    public ResponseEntity<SubjectTopicDTO> create(@RequestBody SubjectTopicFormDTO subjectTopicFormDTO) {
        SubjectTopic subjectTopic = subjectTopicService.save(subjectTopicFormDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(subjectTopicService.convertSubjectTopicToSubjectTopicDetailsDTO(subjectTopic));
    }

    @GetMapping("/all")
    public ResponseEntity<List<SubjectTopicDTO>> listAll() {
        List<SubjectTopic> subjectTopicList = subjectTopicService.findAll();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subjectTopicService.convertToSubjectTopicDetailsDTOList(subjectTopicList));
    }

    @GetMapping
    public ResponseEntity<Page<SubjectTopicDTO>> list(Pageable pageable) {
        Page<SubjectTopic> subjectTopicPage = subjectTopicService.findAll(pageable);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subjectTopicService.convertToSubjectTopicDetailsDTOPage(subjectTopicPage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SubjectTopicDTO> update(@PathVariable Long id, @RequestBody SubjectTopicFormDTO subjectTopicFormDTO) {
        SubjectTopic subjectTopic = subjectTopicService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subjectTopicService.update(subjectTopic, subjectTopicFormDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectTopicDTO> findById(@PathVariable Long id) {
        SubjectTopic subjectTopic = subjectTopicService.findById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(subjectTopicService.convertSubjectTopicToSubjectTopicDetailsDTO(subjectTopic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        SubjectTopic subjectTopic = subjectTopicService.findById(id);
        subjectTopicService.delete(subjectTopic);
        return ResponseEntity.noContent().build();
    }
}