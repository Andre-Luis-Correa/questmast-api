package com.questmast.questmast.core.subjecttopic.mapper;

import com.questmast.questmast.core.subjecttopic.domain.dto.SubjectTopicDTO;
import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import com.questmast.questmast.core.subject.mapper.SubjectMapper;

public class SubjectTopicMapper {

    public static SubjectTopicDTO convertFromEntityToDTO(SubjectTopic subjectTopic) {
        return new SubjectTopicDTO(subjectTopic.getId(), subjectTopic.getName(), subjectTopic.getDescription(), SubjectMapper.convertFromEntityToDTO(subjectTopic.getSubject())
        );
    }
}