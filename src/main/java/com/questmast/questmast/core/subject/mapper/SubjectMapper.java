package com.questmast.questmast.core.subject.mapper;

import com.questmast.questmast.core.subject.domain.dto.SubjectDTO;
import com.questmast.questmast.core.subject.domain.entity.Subject;

public class SubjectMapper {
    public static SubjectDTO convertFromEntityToDTO(Subject subject) {
        return new SubjectDTO(subject.getId(), subject.getName(), subject.getDescription());
    }
}