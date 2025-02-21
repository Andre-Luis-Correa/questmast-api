package com.questmast.questmast.core.contentmoderator.repository;

import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContentModeratorRepository extends JpaRepository<ContentModerator, Long> {

    Optional<ContentModerator> findByMainEmail(String email);
}
