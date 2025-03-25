package com.questmast.questmast.core.solvedselectionprocesstest.repository;

import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.solvedselectionprocesstest.domain.model.SolvedSelectionProcessTest;
import com.questmast.questmast.core.student.domain.model.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SolvedSelectionProcessTestRepository extends JpaRepository<SolvedSelectionProcessTest, Long>, JpaSpecificationExecutor<SolvedSelectionProcessTest> {
    Optional<SolvedSelectionProcessTest> findFirstByStudentAndSelectionProcessTestOrderByEndDateTimeDesc(
            Student student,
            SelectionProcessTest selectionProcessTest
    );
}
