package com.questmast.questmast.core.contact.ddd.repository;

import com.questmast.questmast.core.contact.ddd.domain.DDD;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DDDRepository extends JpaRepository<DDD, Integer> {
    Optional<DDD> findByDdd(Integer ddd);

    List<DDD> findAllByOrderByDdd();
}
