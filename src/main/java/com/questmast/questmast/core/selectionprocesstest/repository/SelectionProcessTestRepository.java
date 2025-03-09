package com.questmast.questmast.core.selectionprocesstest.repository;

import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectionProcessTestRepository extends JpaRepository<SelectionProcessTest, Long>, JpaSpecificationExecutor<SelectionProcessTest> {

    List<SelectionProcessTest> findTop10ByOrderByViewCounterDesc();

    List<SelectionProcessTest> findBySelectionProcessInAndFunction_IdIn(
            List<SelectionProcess> selectionProcessList,
            List<Long> functionIds
    );

    List<SelectionProcessTest> findBySelectionProcessIn(List<SelectionProcess> selectionProcesseList);
}
