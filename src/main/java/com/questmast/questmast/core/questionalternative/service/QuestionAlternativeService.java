package com.questmast.questmast.core.questionalternative.service;

import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeDTO;
import com.questmast.questmast.core.questionalternative.domain.dto.QuestionAlternativeFormDTO;
import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import com.questmast.questmast.core.questionalternative.mapper.QuestionAlternativeMapStructMapper;
import com.questmast.questmast.core.questionalternative.repository.QuestionAlternativeRepository;
//import com.questmast.questmast.exceptions.domain.EntityNotFoundExcpetion;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuestionAlternativeService {

    private final QuestionAlternativeRepository questionAlternativeRepository;
    private final QuestionAlternativeMapStructMapper questionAlternativeMapper;

    public QuestionAlternative findById(Long id) {
        return questionAlternativeRepository.findById(id).orElseThrow(
                //() -> new EntityNotFoundExcpetion("QuestionAlternative", "id", id.toString())
        );
    }

    public QuestionAlternative save(QuestionAlternativeFormDTO questionAlternativeFormDTO) {
        QuestionAlternative questionAlternative = convertFormDTOToEntity(questionAlternativeFormDTO);
        return questionAlternativeRepository.save(questionAlternative);
    }

    private QuestionAlternative convertFormDTOToEntity(QuestionAlternativeFormDTO questionAlternativeFormDTO) {
        return questionAlternativeMapper.convertQuestionAlternativeFormDTOToQuestionAlternative(questionAlternativeFormDTO);
    }

    public QuestionAlternativeDTO convertEntityToDTO(QuestionAlternative questionAlternative) {
        return questionAlternativeMapper.convertQuestionAlternativeToQuestionAlternativeDetailsDTO(questionAlternative);
    }

    public Page<QuestionAlternative> findAll(Pageable pageable) {
        return questionAlternativeRepository.findAll(pageable);
    }

    public Page<QuestionAlternativeDTO> convertToDTOPage(Page<QuestionAlternative> questionAlternativePage) {
        return questionAlternativePage.map(questionAlternativeMapper::convertQuestionAlternativeToQuestionAlternativeDetailsDTO);
    }

    public void delete(QuestionAlternative questionAlternative) {
        questionAlternativeRepository.delete(questionAlternative);
    }

    public List<QuestionAlternative> findAll() {
        return questionAlternativeRepository.findAll();
    }

    public List<QuestionAlternativeDTO> convertToDTOList(List<QuestionAlternative> questionAlternativeList) {
        return questionAlternativeList.stream()
                .map(questionAlternativeMapper::convertQuestionAlternativeToQuestionAlternativeDetailsDTO)
                .collect(Collectors.toList());
    }

    public QuestionAlternativeDTO update(QuestionAlternative questionAlternative, QuestionAlternativeFormDTO questionAlternativeFormDTO) {
        QuestionAlternative updatedQuestionAlternative = convertFormDTOToEntity(questionAlternativeFormDTO);
        updatedQuestionAlternative.setId(questionAlternative.getId());

        questionAlternativeRepository.save(updatedQuestionAlternative);
        return convertEntityToDTO(updatedQuestionAlternative);
    }
}