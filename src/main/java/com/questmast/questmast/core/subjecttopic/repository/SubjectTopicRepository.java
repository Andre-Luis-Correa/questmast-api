package com.questmast.questmast.core.subjecttopic.repository;

import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectTopicRepository extends JpaRepository<SubjectTopic, Long> {
    List<SubjectTopic> findAllByIdIn(List<Long> ids);
}