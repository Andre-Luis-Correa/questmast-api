package com.questmast.questmast.core.contact.ddi.repository;

import com.questmast.questmast.core.contact.ddi.domain.DDI;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DDIRepository extends JpaRepository<DDI, Integer> {
    Optional<DDI> findByDdi(Integer ddi);
}
