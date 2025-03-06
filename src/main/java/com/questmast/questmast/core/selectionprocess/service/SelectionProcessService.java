package com.questmast.questmast.core.selectionprocess.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.city.domain.model.City;
import com.questmast.questmast.core.boardexaminer.domain.model.BoardExaminer;
import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.institution.domain.model.Institution;
import com.questmast.questmast.core.selectionprocess.domain.dto.SelectionProcessFormDTO;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocess.repository.SelectionProcessRepository;
import com.questmast.questmast.core.selectionprocessstatus.domain.entity.SelectionProcessStatus;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SelectionProcessService {

    private final SelectionProcessRepository selectionProcessRepository;

    public SelectionProcess create(SelectionProcessFormDTO selectionProcessFormDTO, BoardExaminer boardExaminer, Institution institution, City city, ContentModerator contentModerator, SelectionProcessStatus selectionProcessStatus) {
        SelectionProcess selectionProcess = new SelectionProcess();
        selectionProcess.setName(selectionProcessFormDTO.name());
        selectionProcess.setOpeningDate(selectionProcessFormDTO.openingDate());
        selectionProcess.setUrl(selectionProcessFormDTO.url());
        selectionProcess.setViewCounter(0);
        selectionProcess.setBoardExaminer(boardExaminer);
        selectionProcess.setInstitution(institution);
        selectionProcess.setCity(city);
        selectionProcess.setContentModerator(contentModerator);
        selectionProcess.setSelectionProcessStatus(selectionProcessStatus);
        selectionProcess.setCreationDate(LocalDateTime.now());

        return selectionProcessRepository.save(selectionProcess);
    }

    public SelectionProcess findById(Long id) {
        SelectionProcess selectionProcess = selectionProcessRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("SelectionProcess", "id", id.toString())
        );

        updateViewCounter(selectionProcess);

        return selectionProcess;
    }

    public void update(SelectionProcess selectionProcess, SelectionProcessFormDTO selectionProcessFormDTO, BoardExaminer boardExaminer, Institution institution, City city, ContentModerator contentModerator, SelectionProcessStatus selectionProcessStatus) {
        selectionProcess.setName(selectionProcessFormDTO.name());
        selectionProcess.setOpeningDate(selectionProcessFormDTO.openingDate());
        selectionProcess.setUrl(selectionProcessFormDTO.url());
        selectionProcess.setBoardExaminer(boardExaminer);
        selectionProcess.setInstitution(institution);
        selectionProcess.setCity(city);
        selectionProcess.setSelectionProcessStatus(selectionProcessStatus);
        selectionProcess.setContentModerator(contentModerator);

        selectionProcessRepository.save(selectionProcess);
    }

    public void updateViewCounter(SelectionProcess selectionProcess) {
        Integer viewCounter  = selectionProcess.getViewCounter();

        if(viewCounter == null) {
            viewCounter = 0;
        } else {
            viewCounter += 1;
        }

        selectionProcess.setViewCounter(viewCounter);

        selectionProcessRepository.save(selectionProcess);
    }

    public void delete(SelectionProcess selectionProcess) {
        selectionProcessRepository.delete(selectionProcess);
    }

    public List<SelectionProcess> list() {
        return selectionProcessRepository.findAllByOrderByCreationDate();
    }

    public Page<SelectionProcess> list(Pageable pageable) {
        return selectionProcessRepository.findAllByOrderByCreationDate(pageable);
    }

    public List<SelectionProcess> findAllByBoardExaminerAndInstitution(List<Long> boardExaminerIds, List<Long> institutionIds) {
        if ((boardExaminerIds == null || boardExaminerIds.isEmpty()) && (institutionIds == null || institutionIds.isEmpty())) {
            return selectionProcessRepository.findAll();
        }

        return selectionProcessRepository.findByBoardExaminer_IdInAndInstitution_IdIn(boardExaminerIds, institutionIds);
    }


    public void updateSelectionProcessStatus(SelectionProcess selectionProcess, SelectionProcessStatus selectionProcessStatus) {
        selectionProcess.setSelectionProcessStatus(selectionProcessStatus);
        selectionProcessRepository.save(selectionProcess);
    }
}
