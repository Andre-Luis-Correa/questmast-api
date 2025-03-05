package com.questmast.questmast.core.subject.domain.dto;

import java.util.List;

public record SubjectFilterDTO(
        Long subjectId,

        List<Long> subjectTopicIds

) {
}
