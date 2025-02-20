package com.questmast.questmast.core.neighborhood.repository;

import com.questmast.questmast.core.neighborhood.domain.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {
}
