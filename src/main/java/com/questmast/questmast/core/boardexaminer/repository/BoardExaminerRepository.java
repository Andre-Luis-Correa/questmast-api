package com.questmast.questmast.core.boardexaminer.repository;

import com.questmast.questmast.core.boardexaminer.domain.model.BoardExaminer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardExaminerRepository extends JpaRepository<BoardExaminer, Long> {
}
