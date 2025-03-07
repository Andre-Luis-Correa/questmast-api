package com.questmast.questmast.core.function.controller;

import com.questmast.questmast.core.function.domain.dto.FunctionFormDTO;
import com.questmast.questmast.core.function.domain.model.Function;
import com.questmast.questmast.core.function.service.FunctionService;
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
@RequestMapping("/function")
public class FunctionController {

    private final FunctionService functionService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody FunctionFormDTO functionFormDTO) {
        functionService.create(functionFormDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody FunctionFormDTO functionFormDTO) {
        Function function = functionService.findById(id);
        functionService.update(function, functionFormDTO);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Function> getById(@PathVariable Long id) {
        Function function = functionService.findById(id);

        return ResponseEntity.ok().body(function);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<Function>> list(Pageable pageable) {
        Page<Function> functionList = functionService.findAll(pageable);

        return ResponseEntity.ok().body(functionList);
    }

    @GetMapping
    public ResponseEntity<List<Function>> list() {
        List<Function> functionList = functionService.findAll();

        return ResponseEntity.ok().body(functionList);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Function function = functionService.findById(id);
        functionService.delete(function);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
