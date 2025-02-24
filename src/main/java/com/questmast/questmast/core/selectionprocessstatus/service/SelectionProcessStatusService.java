package com.questmast.questmast.core.selectionprocessstatus.service;

//import com.questmast.questmast.core.exceptions.type.EntityNotFoundException;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.selectionprocessstatus.domain.dto.SelectionProcessStatusDTO;
import com.questmast.questmast.core.selectionprocessstatus.domain.dto.SelectionProcessStatusFormDTO;
import com.questmast.questmast.core.selectionprocessstatus.domain.entity.SelectionProcessStatus;
import com.questmast.questmast.core.selectionprocessstatus.mapper.SelectionProcessStatusMapStructMapper;
import com.questmast.questmast.core.selectionprocessstatus.repository.SelectionProcessStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SelectionProcessStatusService {

    private final SelectionProcessStatusRepository selectionProcessStatusRepository;
    private final SelectionProcessStatusMapStructMapper selectionProcessStatusMapper;

    public SelectionProcessStatus findById(Long id) {
        return selectionProcessStatusRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("SelectionProcessStatus", "id", id.toString())
        );
    }

    public SelectionProcessStatus save(SelectionProcessStatusFormDTO selectionProcessStatusFormDTO) {
        SelectionProcessStatus selectionProcessStatus = convertFormDTOToEntity(selectionProcessStatusFormDTO);
        return selectionProcessStatusRepository.save(selectionProcessStatus);
    }

    private SelectionProcessStatus convertFormDTOToEntity(SelectionProcessStatusFormDTO selectionProcessStatusFormDTO) {
        return selectionProcessStatusMapper.convertSelectionProcessStatusFormDTOToSelectionProcessStatus(selectionProcessStatusFormDTO);
    }

    public SelectionProcessStatusDTO convertEntityToDTO(SelectionProcessStatus selectionProcessStatus) {
        return selectionProcessStatusMapper.convertSelectionProcessStatusToSelectionProcessStatusDTO(selectionProcessStatus);
    }

    public Page<SelectionProcessStatus> findAll(Pageable pageable) {
        return selectionProcessStatusRepository.findAll(pageable);
    }

    public Page<SelectionProcessStatusDTO> convertToDTOPage(Page<SelectionProcessStatus> selectionProcessStatusPage) {
        return selectionProcessStatusPage.map(selectionProcessStatusMapper::convertSelectionProcessStatusToSelectionProcessStatusDTO);
    }

    public void delete(SelectionProcessStatus selectionProcessStatus) {
        selectionProcessStatusRepository.delete(selectionProcessStatus);
    }

    public List<SelectionProcessStatus> findAll() {
        return selectionProcessStatusRepository.findAll();
    }

    public List<SelectionProcessStatusDTO> convertToDTOList(List<SelectionProcessStatus> selectionProcessStatusList) {
        return selectionProcessStatusList.stream()
                .map(selectionProcessStatusMapper::convertSelectionProcessStatusToSelectionProcessStatusDTO)
                .collect(Collectors.toList());
    }

    public SelectionProcessStatusDTO update(SelectionProcessStatus selectionProcessStatus, SelectionProcessStatusFormDTO selectionProcessStatusFormDTO) {
        SelectionProcessStatus updatedSelectionProcessStatus = convertFormDTOToEntity(selectionProcessStatusFormDTO);
        updatedSelectionProcessStatus.setId(selectionProcessStatus.getId());

        selectionProcessStatusRepository.save(updatedSelectionProcessStatus);
        return convertEntityToDTO(updatedSelectionProcessStatus);
    }
}