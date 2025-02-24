package com.questmast.questmast.core.selectionprocess.repository;

import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectionProcessRepository extends JpaRepository<SelectionProcess, Long> {
}
