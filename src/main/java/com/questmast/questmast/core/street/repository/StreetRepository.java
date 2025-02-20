package com.questmast.questmast.core.street.repository;

import com.questmast.questmast.core.street.domain.Street;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StreetRepository extends JpaRepository<Street, Long> {
}
