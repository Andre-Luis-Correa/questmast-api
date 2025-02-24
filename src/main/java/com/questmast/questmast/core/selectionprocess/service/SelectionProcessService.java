package com.questmast.questmast.core.selectionprocess.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.city.domain.City;
import com.questmast.questmast.core.boardexaminer.domain.BoardExaminer;
import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.selectionprocess.domain.dto.SelectionProcessFormDTO;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocess.repository.SelectionProcessRepository;
import com.questmast.questmast.core.selectionprocessstatus.domain.entity.SelectionProcessStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SelectionProcessService {

    private final SelectionProcessRepository selectionProcessRepository;

    public void create(SelectionProcessFormDTO selectionProcessFormDTO, BoardExaminer boardExaminer, City city, ContentModerator contentModerator, SelectionProcessStatus selectionProcessStatus) {
        SelectionProcess selectionProcess = new SelectionProcess();
        selectionProcess.setName(selectionProcessFormDTO.name());
        selectionProcess.setUrl(selectionProcessFormDTO.url());
        selectionProcess.setViewCounter(0);
        selectionProcess.setBoardExaminer(boardExaminer);
        selectionProcess.setCity(city);
        selectionProcess.setContentModerator(contentModerator);
        selectionProcess.setSelectionProcessStatus(selectionProcessStatus);

        selectionProcessRepository.save(selectionProcess);
    }

    public SelectionProcess findById(Long id) {
        return selectionProcessRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("SelectionProcess", "id", id.toString())
        );
    }

    public void update(SelectionProcess selectionProcess, SelectionProcessFormDTO selectionProcessFormDTO, BoardExaminer boardExaminer, City city, ContentModerator contentModerator, SelectionProcessStatus selectionProcessStatus) {
        selectionProcess.setName(selectionProcessFormDTO.name());
        selectionProcess.setUrl(selectionProcessFormDTO.url());
        selectionProcess.setBoardExaminer(boardExaminer);
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
        return selectionProcessRepository.findAll();
    }

    public Page<SelectionProcess> list(Pageable pageable) {
        return selectionProcessRepository.findAll(pageable);
    }
}
