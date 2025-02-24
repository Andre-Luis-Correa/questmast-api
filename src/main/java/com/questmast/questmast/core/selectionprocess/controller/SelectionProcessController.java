package com.questmast.questmast.core.selectionprocess.controller;

import com.questmast.questmast.core.address.city.domain.City;
import com.questmast.questmast.core.address.city.service.CityService;
import com.questmast.questmast.core.boardexaminer.domain.BoardExaminer;
import com.questmast.questmast.core.boardexaminer.service.BoardExaminerService;
import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.contentmoderator.service.ContentModeratorService;
import com.questmast.questmast.core.institution.domain.model.Institution;
import com.questmast.questmast.core.institution.service.InstitutionService;
import com.questmast.questmast.core.selectionprocess.domain.dto.SelectionProcessFormDTO;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocess.service.SelectionProcessService;
import com.questmast.questmast.core.selectionprocessstatus.domain.entity.SelectionProcessStatus;
import com.questmast.questmast.core.selectionprocessstatus.service.SelectionProcessStatusService;
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
@RequestMapping("/selection-process")
public class SelectionProcessController {

    private final SelectionProcessService selectionProcessService;
    private final BoardExaminerService boardExaminerService;
    private final InstitutionService institutionService;
    private final ContentModeratorService contentModeratorService;
    private final SelectionProcessStatusService selectionProcessStatusService;
    private final CityService cityService;

    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SelectionProcessFormDTO selectionProcessFormDTO) {
        BoardExaminer boardExaminer = boardExaminerService.findById(selectionProcessFormDTO.boardExaminerId());
        Institution institution = institutionService.findById(selectionProcessFormDTO.institutionId());
        City city = cityService.findById(selectionProcessFormDTO.cityId());
        ContentModerator contentModerator = contentModeratorService.findById(selectionProcessFormDTO.contentModeratorId());
        SelectionProcessStatus selectionProcessStatus = selectionProcessStatusService.findById(selectionProcessFormDTO.selectionProcessStatusId());

        selectionProcessService.create(selectionProcessFormDTO, boardExaminer, institution, city, contentModerator, selectionProcessStatus);
        institutionService.addQuantityOfSelectionProcess(institution);
        boardExaminerService.addQuantityOfSelectionProcess(boardExaminer);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/all")
    public ResponseEntity<Page<SelectionProcess>> list(Pageable pageable) {
        return ResponseEntity.ok().body(selectionProcessService.list(pageable));
    }

    @GetMapping
    public ResponseEntity<List<SelectionProcess>> list() {
        return ResponseEntity.ok().body(selectionProcessService.list());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SelectionProcess> getById(@PathVariable Long id) {
        return ResponseEntity.ok().body(selectionProcessService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @Valid @RequestBody SelectionProcessFormDTO selectionProcessFormDTO) {
        SelectionProcess selectionProcess = selectionProcessService.findById(id);
        BoardExaminer boardExaminer = boardExaminerService.findById(selectionProcessFormDTO.boardExaminerId());
        Institution institution = institutionService.findById(selectionProcessFormDTO.institutionId());
        City city = cityService.findById(selectionProcessFormDTO.cityId());
        ContentModerator contentModerator = contentModeratorService.findById(selectionProcessFormDTO.contentModeratorId());
        SelectionProcessStatus selectionProcessStatus = selectionProcessStatusService.findById(selectionProcessFormDTO.selectionProcessStatusId());

        selectionProcessService.update(selectionProcess, selectionProcessFormDTO, boardExaminer, institution, city, contentModerator, selectionProcessStatus);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/view-counter/{id}")
    public ResponseEntity<Void> updateViewCounter(@PathVariable Long id) {
        SelectionProcess selectionProcess = selectionProcessService.findById(id);

        selectionProcessService.updateViewCounter(selectionProcess);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        SelectionProcess selectionProcess = selectionProcessService.findById(id);
        institutionService.subQuantityOfSelectionProcess(selectionProcess.getInstitution());

        selectionProcessService.delete(selectionProcess);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
