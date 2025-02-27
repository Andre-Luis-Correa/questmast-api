package com.questmast.questmast.core.boardexaminer.controller;

import com.questmast.questmast.core.boardexaminer.domain.dto.BoardExaminerFormDTO;
import com.questmast.questmast.core.boardexaminer.domain.model.BoardExaminer;
import com.questmast.questmast.core.boardexaminer.service.BoardExaminerService;
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
@RequestMapping("/board-examiner")
public class BoardExaminerController {

    private final BoardExaminerService boardExaminerService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody BoardExaminerFormDTO boardExaminerFormDTO) {
        boardExaminerService.create(boardExaminerFormDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody BoardExaminerFormDTO boardExaminerFormDTO) {
        BoardExaminer boardExaminer = boardExaminerService.findById(id);

        boardExaminerService.update(boardExaminer, boardExaminerFormDTO);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        BoardExaminer boardExaminer = boardExaminerService.findById(id);

        boardExaminerService.delete(boardExaminer);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoardExaminer> getById(@PathVariable Long id) {
        BoardExaminer boardExaminer = boardExaminerService.findById(id);
        return ResponseEntity.ok().body(boardExaminer);
    }

    @GetMapping()
    public ResponseEntity<List<BoardExaminer>> list() {
        List<BoardExaminer> boardExaminerList = boardExaminerService.findAll();

        return ResponseEntity.ok().body(boardExaminerList);
    }

    @GetMapping("/all")
    public ResponseEntity<Page<BoardExaminer>> list(Pageable pageable) {
        Page<BoardExaminer> boardExaminerList = boardExaminerService.findAll(pageable);

        return ResponseEntity.ok().body(boardExaminerList);
    }
}
