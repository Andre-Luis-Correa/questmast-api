package com.questmast.questmast.core.professionallevel.mapper;

import com.questmast.questmast.core.professionallevel.domain.dto.ProfessionalLevelDTO;
import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;

public class ProfessionalLevelMapper {
    public static ProfessionalLevelDTO convertFromEntityToDTO(ProfessionalLevel level) {
        return new ProfessionalLevelDTO(level.getId(), level.getName(), level.getDescription());
    }
}