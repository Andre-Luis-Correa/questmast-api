package com.questmast.questmast.core.subject.mapper;

import com.questmast.questmast.core.subject.domain.dto.SubjectDTO;
import com.questmast.questmast.core.subject.domain.dto.SubjectFormDTO;
import com.questmast.questmast.core.subject.domain.entity.Subject;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectMapStructMapper {
    Subject convertSubjectFormDTOToSubject(SubjectFormDTO subjectFormDTO);

    SubjectDTO convertSubjectToSubjectDetailsDTO(Subject subject);
}