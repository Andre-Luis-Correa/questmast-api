package com.questmast.questmast.core.selectionprocessstatus.controller;

import com.questmast.questmast.core.selectionprocessstatus.domain.dto.SelectionProcessStatusDTO;
import com.questmast.questmast.core.selectionprocessstatus.domain.dto.SelectionProcessStatusFormDTO;
import com.questmast.questmast.core.selectionprocessstatus.domain.entity.SelectionProcessStatus;
import com.questmast.questmast.core.selectionprocessstatus.service.SelectionProcessStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/selection-process-status")
@RequiredArgsConstructor
public class SelectionProcessStatusController {

    private final SelectionProcessStatusService selectionProcessStatusService;

    @PostMapping
    public ResponseEntity<SelectionProcessStatusDTO> create(@RequestBody SelectionProcessStatusFormDTO formDTO) {
        SelectionProcessStatus status = selectionProcessStatusService.save(formDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(selectionProcessStatusService.convertEntityToDTO(status));
    }

    @GetMapping
    public ResponseEntity<List<SelectionProcessStatusDTO>> listAll() {
        List<SelectionProcessStatus> statusList = selectionProcessStatusService.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(selectionProcessStatusService.convertToDTOList(statusList));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<SelectionProcessStatusDTO>> list(Pageable pageable) {
        Page<SelectionProcessStatus> statusPage = selectionProcessStatusService.findAll(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(selectionProcessStatusService.convertToDTOPage(statusPage));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SelectionProcessStatusDTO> update(@PathVariable Long id, @RequestBody SelectionProcessStatusFormDTO formDTO) {
        SelectionProcessStatus status = selectionProcessStatusService.findById(id);
        return ResponseEntity.status(HttpStatus.OK).body(selectionProcessStatusService.update(status, formDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SelectionProcessStatusDTO> findById(@PathVariable Long id) {
        SelectionProcessStatus status = selectionProcessStatusService.findById(id);
        return ResponseEntity.ok(selectionProcessStatusService.convertEntityToDTO(status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        SelectionProcessStatus status = selectionProcessStatusService.findById(id);
        selectionProcessStatusService.delete(status);
        return ResponseEntity.noContent().build();
    }
}