package com.questmast.questmast.core.subject.service;

import com.questmast.questmast.core.subject.domain.dto.SubjectDTO;
import com.questmast.questmast.core.subject.domain.dto.SubjectFormDTO;
import com.questmast.questmast.core.subject.domain.entity.Subject;
import com.questmast.questmast.core.subject.mapper.SubjectMapStructMapper;
import com.questmast.questmast.core.subject.repository.SubjectRepository;
//import com.questmast.questmast.exceptions.type.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepository subjectRepository;
    private final SubjectMapStructMapper subjectMapStructMapper;

    public Subject findById(Long id) {
        return subjectRepository.findById(id).orElseThrow(
                //() -> new EntityNotFoundException("Subject", "id", id.toString())
        );
    }

    public Subject save(SubjectFormDTO subjectFormDTO) {
        Subject subject = convertSubjectFormDTOToSubject(subjectFormDTO);
        return subjectRepository.save(subject);
    }

    private Subject convertSubjectFormDTOToSubject(SubjectFormDTO subjectFormDTO) {
        return subjectMapStructMapper.convertSubjectFormDTOToSubject(subjectFormDTO);
    }

    public SubjectDTO convertSubjectToSubjectDetailsDTO(Subject subject) {
        return subjectMapStructMapper.convertSubjectToSubjectDetailsDTO(subject);
    }

    public Page<Subject> findAll(Pageable pageable) {
        return subjectRepository.findAll(pageable);
    }

    public Page<SubjectDTO> convertToSubjectDetailsDTOPage(Page<Subject> subjectPage) {
        return subjectPage.map(subjectMapStructMapper::convertSubjectToSubjectDetailsDTO);
    }

    public void delete(Subject subject) {
        subjectRepository.delete(subject);
    }

    public List<Subject> findAll() {
        return subjectRepository.findAll();
    }

    public List<SubjectDTO> convertToSubjectDetailsDTOList(List<Subject> subjectList) {
        return subjectList.stream().map(subjectMapStructMapper::convertSubjectToSubjectDetailsDTO).collect(Collectors.toList());
    }

    public SubjectDTO update(Subject subject, SubjectFormDTO subjectFormDTO) {
        Subject updatedSubject = convertSubjectFormDTOToSubject(subjectFormDTO);
        updatedSubject.setId(subject.getId());

        subjectRepository.save(updatedSubject);
        return convertSubjectToSubjectDetailsDTO(updatedSubject);
    }
}