package com.questmast.questmast.core.solvedselectionprocesstest.repository;

import com.questmast.questmast.core.solvedselectionprocesstest.domain.model.SolvedSelectionProcessTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SolvedSelectionProcessTestRepository extends JpaRepository<SolvedSelectionProcessTest, Long>, JpaSpecificationExecutor<SolvedSelectionProcessTest> {
}
