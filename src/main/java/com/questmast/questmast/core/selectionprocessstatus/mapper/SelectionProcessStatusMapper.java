package com.questmast.questmast.core.selectionprocessstatus.mapper;

import com.questmast.questmast.core.selectionprocessstatus.domain.dto.SelectionProcessStatusDTO;
import com.questmast.questmast.core.selectionprocessstatus.domain.entity.SelectionProcessStatus;

public class SelectionProcessStatusMapper {
    public static SelectionProcessStatusDTO convertFromEntityToDTO(SelectionProcessStatus status) {
        return new SelectionProcessStatusDTO(status.getId(), status.getDescription());
    }
}
