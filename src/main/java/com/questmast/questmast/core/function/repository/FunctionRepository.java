package com.questmast.questmast.core.function.repository;

import com.questmast.questmast.core.function.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FunctionRepository extends JpaRepository<Function, Long> {
}
