package com.questmast.questmast.core.selectionprocessstatus.repository;

import com.questmast.questmast.core.selectionprocessstatus.domain.entity.SelectionProcessStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectionProcessStatusRepository extends JpaRepository<SelectionProcessStatus, Long> {
}
