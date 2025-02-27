package com.questmast.questmast.core.selectionprocess.repository;

import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SelectionProcessRepository extends JpaRepository<SelectionProcess, Long> {
    Page<SelectionProcess> findAllByOrderByCreationDate(Pageable pageable);

    List<SelectionProcess> findAllByOrderByCreationDate();
}
