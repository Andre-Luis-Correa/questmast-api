package com.questmast.questmast.core.subjecttopic.service;

//import com.questmast.questmast.core.exceptions.domain.EntityNotFoundException;
import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicDTO;
import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicFormDTO;
import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import com.questmast.questmast.core.subjecttopic.mapper.SubjectTopicMapStructMapper;
import com.questmast.questmast.core.subjecttopic.repository.SubjectTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectTopicService {

    private final SubjectTopicRepository subjectTopicRepository;
    private final SubjectTopicMapStructMapper subjectTopicMapStructMapper;

    public SubjectTopic findById(Long id) {
        return subjectTopicRepository.findById(id).orElseThrow(
                //() -> new EntityNotFoundException("SelectionProcessStatus", "id", id.toString())
        );
    }

    public SubjectTopic save(SubjectTopicFormDTO subjectTopicFormDTO) {
        SubjectTopic subjectTopic = convertSubjectTopicFormDTOToSubjectTopic(subjectTopicFormDTO);
        return subjectTopicRepository.save(subjectTopic);
    }

    private SubjectTopic convertSubjectTopicFormDTOToSubjectTopic(SubjectTopicFormDTO subjectTopicFormDTO) {
        return subjectTopicMapStructMapper.convertSubjectTopicFormDTOToSubjectTopic(subjectTopicFormDTO);
    }

    public SubjectTopicDTO convertSubjectTopicToSubjectTopicDetailsDTO(SubjectTopic subjectTopic) {
        return subjectTopicMapStructMapper.convertSubjectTopicToSubjectTopicDetailsDTO(subjectTopic);
    }

    public Page<SubjectTopic> findAll(Pageable pageable) {
        return subjectTopicRepository.findAll(pageable);
    }

    public Page<SubjectTopicDTO> convertToSubjectTopicDetailsDTOPage(Page<SubjectTopic> subjectTopicPage) {
        return subjectTopicPage.map(subjectTopicMapStructMapper::convertSubjectTopicToSubjectTopicDetailsDTO);
    }

    public void delete(SubjectTopic subjectTopic) {
        subjectTopicRepository.delete(subjectTopic);
    }

    public List<SubjectTopic> findAll() {
        return subjectTopicRepository.findAll();
    }

    public List<SubjectTopicDTO> convertToSubjectTopicDetailsDTOList(List<SubjectTopic> subjectTopicList) {
        return subjectTopicList.stream()
                .map(subjectTopicMapStructMapper::convertSubjectTopicToSubjectTopicDetailsDTO)
                .collect(Collectors.toList());
    }

    public SubjectTopicDTO update(SubjectTopic subjectTopic, SubjectTopicFormDTO subjectTopicFormDTO) {
        SubjectTopic updatedSubjectTopic = convertSubjectTopicFormDTOToSubjectTopic(subjectTopicFormDTO);
        updatedSubjectTopic.setId(subjectTopic.getId());
        subjectTopicRepository.save(updatedSubjectTopic);
        return convertSubjectTopicToSubjectTopicDetailsDTO(updatedSubjectTopic);
    }
}