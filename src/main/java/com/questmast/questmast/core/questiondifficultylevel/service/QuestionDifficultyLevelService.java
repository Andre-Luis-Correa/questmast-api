package com.questmast.questmast.core.questiondifficultylevel.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.questiondifficultylevel.domain.dto.QuestionDifficultyLevelDTO;
import com.questmast.questmast.core.questiondifficultylevel.domain.dto.QuestionDifficultyLevelFormDTO;
import com.questmast.questmast.core.questiondifficultylevel.domain.entity.QuestionDifficultyLevel;
import com.questmast.questmast.core.questiondifficultylevel.mapper.QuestionDifficultyLevelMapStructMapper;
import com.questmast.questmast.core.questiondifficultylevel.repository.QuestionDifficultyLevelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionDifficultyLevelService {

    private final QuestionDifficultyLevelRepository questionDifficultyLevelRepository;
    private final QuestionDifficultyLevelMapStructMapper questionDifficultyLevelMapper;

    public QuestionDifficultyLevel findById(Long id) {
        return questionDifficultyLevelRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("QuestionDifficultyLevel", "id", id.toString())
        );
    }

    public QuestionDifficultyLevel save(QuestionDifficultyLevelFormDTO questionDifficultyLevelFormDTO) {
        QuestionDifficultyLevel questionDifficultyLevel = convertQuestionDifficultyLevelFormDTOToEntity(questionDifficultyLevelFormDTO);
        return questionDifficultyLevelRepository.save(questionDifficultyLevel);
    }

    private QuestionDifficultyLevel convertQuestionDifficultyLevelFormDTOToEntity(QuestionDifficultyLevelFormDTO formDTO) {
        return questionDifficultyLevelMapper.convertQuestionDifficultyLevelFormDTOToQuestionDifficultyLevel(formDTO);
    }

    public QuestionDifficultyLevelDTO convertEntityToDTO(QuestionDifficultyLevel questionDifficultyLevel) {
        return questionDifficultyLevelMapper.convertQuestionDifficultyLevelToQuestionDifficultyLevelDetailsDTO(questionDifficultyLevel);
    }

    public Page<QuestionDifficultyLevel> findAll(Pageable pageable) {
        return questionDifficultyLevelRepository.findAll(pageable);
    }

    public Page<QuestionDifficultyLevelDTO> convertToDTOPage(Page<QuestionDifficultyLevel> questionDifficultyLevelPage) {
        return questionDifficultyLevelPage.map(questionDifficultyLevelMapper::convertQuestionDifficultyLevelToQuestionDifficultyLevelDetailsDTO);
    }

    public void delete(QuestionDifficultyLevel questionDifficultyLevel) {
        questionDifficultyLevelRepository.delete(questionDifficultyLevel);
    }

    public List<QuestionDifficultyLevel> findAll() {
        return questionDifficultyLevelRepository.findAll();
    }

    public List<QuestionDifficultyLevelDTO> convertToDTOList(List<QuestionDifficultyLevel> questionDifficultyLevelList) {
        return questionDifficultyLevelList.stream()
                .map(questionDifficultyLevelMapper::convertQuestionDifficultyLevelToQuestionDifficultyLevelDetailsDTO)
                .collect(Collectors.toList());
    }

    public QuestionDifficultyLevelDTO update(QuestionDifficultyLevel questionDifficultyLevel, QuestionDifficultyLevelFormDTO formDTO) {
        QuestionDifficultyLevel updatedEntity = convertQuestionDifficultyLevelFormDTOToEntity(formDTO);
        updatedEntity.setId(questionDifficultyLevel.getId());

        questionDifficultyLevelRepository.save(updatedEntity);
        return convertEntityToDTO(updatedEntity);
    }

    public List<QuestionDifficultyLevel> findAllByIdIn(List<Long> ids) {
        return questionDifficultyLevelRepository.findAllByIdIn(ids);
    }
}