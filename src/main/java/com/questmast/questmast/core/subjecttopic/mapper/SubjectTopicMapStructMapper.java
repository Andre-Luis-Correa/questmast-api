package com.questmast.questmast.core.subjecttopic.mapper;

import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicDTO;
import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicFormDTO;
import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SubjectTopicMapStructMapper {
    SubjectTopic convertSubjectTopicFormDTOToSubjectTopic(SubjectTopicFormDTO subjectTopicFormDTO);

    SubjectTopicDTO convertSubjectTopicToSubjectTopicDetailsDTO(SubjectTopic subjectTopic);
}