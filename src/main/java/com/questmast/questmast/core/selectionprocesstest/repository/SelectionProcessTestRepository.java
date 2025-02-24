package com.questmast.questmast.core.selectionprocesstest.repository;

import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SelectionProcessTestRepository extends JpaRepository<SelectionProcessTest, Long> {
}
