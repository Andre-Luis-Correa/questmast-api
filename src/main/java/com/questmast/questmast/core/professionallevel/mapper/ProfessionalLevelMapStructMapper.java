package com.questmast.questmast.core.professionallevel.mapper;

import com.questmast.questmast.core.professionallevel.domain.dto.ProfessionalLevelDTO;
import com.questmast.questmast.core.professionallevel.domain.dto.ProfessionalLevelFormDTO;
import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProfessionalLevelMapStructMapper {

    ProfessionalLevel convertProfessionalLevelFormDTOToProfessionalLevel(ProfessionalLevelFormDTO professionalLevelFormDTO);

    ProfessionalLevelDTO convertProfessionalLevelToProfessionalLevelDTO(ProfessionalLevel professionalLevel);
}