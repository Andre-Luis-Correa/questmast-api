package com.questmast.questmast.core.subjecttopic.repository;

import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubjectTopicRepository extends JpaRepository<SubjectTopic, Long> {
}