package com.questmast.questmast.core.professionallevel.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.professionallevel.domain.dto.ProfessionalLevelDTO;
import com.questmast.questmast.core.professionallevel.domain.dto.ProfessionalLevelFormDTO;
import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;
import com.questmast.questmast.core.professionallevel.mapper.ProfessionalLevelMapStructMapper;
import com.questmast.questmast.core.professionallevel.repository.ProfessionalLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfessionalLevelService {

    private final ProfessionalLevelRepository professionalLevelRepository;
    private final ProfessionalLevelMapStructMapper professionalLevelMapper;

    public ProfessionalLevel findById(Long id) {
        return professionalLevelRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("ProfessionalLevel", "id", id.toString())
        );
    }

    public ProfessionalLevel save(ProfessionalLevelFormDTO professionalLevelFormDTO) {
        ProfessionalLevel professionalLevel = professionalLevelMapper.convertProfessionalLevelFormDTOToProfessionalLevel(professionalLevelFormDTO);
        return professionalLevelRepository.save(professionalLevel);
    }

    public ProfessionalLevelDTO convertProfessionalLevelToDTO(ProfessionalLevel professionalLevel) {
        return professionalLevelMapper.convertProfessionalLevelToProfessionalLevelDTO(professionalLevel);
    }

    public Page<ProfessionalLevel> findAll(Pageable pageable) {
        return professionalLevelRepository.findAll(pageable);
    }

    public Page<ProfessionalLevelDTO> convertToProfessionalLevelDTOPage(Page<ProfessionalLevel> professionalLevelPage) {
        return professionalLevelPage.map(professionalLevelMapper::convertProfessionalLevelToProfessionalLevelDTO);
    }

    public void delete(ProfessionalLevel professionalLevel) {
        professionalLevelRepository.delete(professionalLevel);
    }

    public List<ProfessionalLevel> findAll() {
        return professionalLevelRepository.findAll();
    }

    public List<ProfessionalLevelDTO> convertToProfessionalLevelDTOList(List<ProfessionalLevel> professionalLevelList) {
        return professionalLevelList.stream()
                .map(professionalLevelMapper::convertProfessionalLevelToProfessionalLevelDTO)
                .collect(Collectors.toList());
    }

    public ProfessionalLevelDTO update(ProfessionalLevel professionalLevel, ProfessionalLevelFormDTO professionalLevelFormDTO) {
        ProfessionalLevel updatedProfessionalLevel = professionalLevelMapper.convertProfessionalLevelFormDTOToProfessionalLevel(professionalLevelFormDTO);
        updatedProfessionalLevel.setId(professionalLevel.getId());
        professionalLevelRepository.save(updatedProfessionalLevel);
        return convertProfessionalLevelToDTO(updatedProfessionalLevel);
    }
}