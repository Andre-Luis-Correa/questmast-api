package com.questmast.questmast.core.selectionprocesstest.controller;

import com.questmast.questmast.core.function.Function;
import com.questmast.questmast.core.function.service.FunctionService;
import com.questmast.questmast.core.selectionprocesstest.domain.dto.SelectionProcessTestFormDTO;
import com.questmast.questmast.core.selectionprocesstest.service.SelectionProcessTestService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/selection-process-test")
public class SelectionProcessTestController {

    private final SelectionProcessTestService selectionProcessTestService;
    private final FunctionService functionService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SelectionProcessTestFormDTO selectionProcessTestFormDTO) {
        Function function = functionService.findById(selectionProcessTestFormDTO.functionId());
    }
}
