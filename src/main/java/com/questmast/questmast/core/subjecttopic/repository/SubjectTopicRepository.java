package com.questmast.questmast.core.subjecttopic.repository;

import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectTopicRepository extends JpaRepository<SubjectTopic, Long>, JpaSpecificationExecutor<SubjectTopic> {
    List<SubjectTopic> findAllByIdIn(List<Long> ids);
}