package com.questmast.questmast.core.function.repository;

import com.questmast.questmast.core.function.domain.model.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {
}
