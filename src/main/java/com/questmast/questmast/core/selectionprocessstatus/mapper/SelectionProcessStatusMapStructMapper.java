package com.questmast.questmast.core.selectionprocessstatus.mapper;

import com.questmast.questmast.core.selectionprocessstatus.domain.dto.SelectionProcessStatusDTO;
import com.questmast.questmast.core.selectionprocessstatus.domain.dto.SelectionProcessStatusFormDTO;
import com.questmast.questmast.core.selectionprocessstatus.domain.entity.SelectionProcessStatus;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SelectionProcessStatusMapStructMapper {

    SelectionProcessStatus convertSelectionProcessStatusFormDTOToSelectionProcessStatus(SelectionProcessStatusFormDTO formDTO);

    SelectionProcessStatusDTO convertSelectionProcessStatusToSelectionProcessStatusDTO(SelectionProcessStatus status);
}
