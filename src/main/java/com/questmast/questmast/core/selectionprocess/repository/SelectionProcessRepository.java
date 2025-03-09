package com.questmast.questmast.core.selectionprocess.repository;

import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectionProcessRepository extends JpaRepository<SelectionProcess, Long>, JpaSpecificationExecutor<SelectionProcess> {
    Page<SelectionProcess> findAllByOrderByCreationDate(Pageable pageable);

    List<SelectionProcess> findAllByOrderByCreationDate();

    List<SelectionProcess> findByBoardExaminer_IdInAndInstitution_IdIn(
            List<Long> boardExaminerIds,
            List<Long> institutionIds
    );

    List<SelectionProcess> findTop10ByOrderByViewCounterDesc();

    List<SelectionProcess> findByBoardExaminer_IdIn(List<Long> boardExaminerIds);

    List<SelectionProcess> findByInstitution_IdIn(List<Long> institutionIds);
}
