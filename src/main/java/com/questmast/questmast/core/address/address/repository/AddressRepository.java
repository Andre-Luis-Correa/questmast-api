package com.questmast.questmast.core.address.address.repository;

import com.questmast.questmast.core.address.address.domain.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
}
