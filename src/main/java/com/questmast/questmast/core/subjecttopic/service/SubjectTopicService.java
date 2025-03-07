package com.questmast.questmast.core.subjecttopic.service;

import com.questmast.questmast.common.specification.BaseSpecification;
import com.questmast.questmast.common.specification.Search;
import com.questmast.questmast.common.specification.SpecificationUtils;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicDTO;
import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicFilterDTO;
import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicFormDTO;
import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import com.questmast.questmast.core.subjecttopic.mapper.SubjectTopicMapStructMapper;
import com.questmast.questmast.core.subjecttopic.repository.SubjectTopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
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

    public Page<SubjectTopic> findAll(Pageable pageable, SubjectTopicFilterDTO subjectTopicFilterDTO) {
        Specification<SubjectTopic> subjectTopicSpecification = generateSpecification(subjectTopicFilterDTO);
        Pageable sortedPageable = PageRequest.of(
                pageable.getPageNumber(),
                pageable.getPageSize(),
                Sort.by(Sort.Direction.ASC, "name")
        );

        return subjectTopicRepository.findAll(subjectTopicSpecification, sortedPageable);
    }

    public Page<SubjectTopicDTO> convertToSubjectTopicDetailsDTOPage(Page<SubjectTopic> subjectTopicPage) {
        return subjectTopicPage.map(subjectTopicMapStructMapper::convertSubjectTopicToSubjectTopicDetailsDTO);
    }

    public void delete(SubjectTopic subjectTopic) {
        subjectTopicRepository.delete(subjectTopic);
    }

    public List<SubjectTopic> findAll(SubjectTopicFilterDTO subjectTopicFilterDTO) {
        Specification<SubjectTopic> subjectTopicSpecification = generateSpecification(subjectTopicFilterDTO);
        Sort sort = Sort.by(Sort.Direction.ASC, "name");

        return subjectTopicRepository.findAll(subjectTopicSpecification, sort);
    }

    private Specification<SubjectTopic> generateSpecification(SubjectTopicFilterDTO subjectTopicFilterDTO) {
        Search<Long> subjectCriteria = SpecificationUtils.generateEqualsCriteria("subject.id", subjectTopicFilterDTO.subjectId());

        Specification<SubjectTopic> subjectSpecification = new BaseSpecification<>(subjectCriteria);

        return Specification.where(subjectSpecification);
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

    public List<SubjectTopic> findAllByIdIn(List<Long> ids) {
        return subjectTopicRepository.findAllByIdIn(ids);
    }
}