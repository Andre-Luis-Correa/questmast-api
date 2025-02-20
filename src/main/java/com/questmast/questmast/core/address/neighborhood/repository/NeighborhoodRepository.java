package com.questmast.questmast.core.address.neighborhood.repository;

import com.questmast.questmast.core.address.neighborhood.domain.Neighborhood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NeighborhoodRepository extends JpaRepository<Neighborhood, Long> {
}
