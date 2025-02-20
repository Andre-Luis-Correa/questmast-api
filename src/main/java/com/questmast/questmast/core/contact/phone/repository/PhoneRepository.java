package com.questmast.questmast.core.contact.phone.repository;

import com.questmast.questmast.core.contact.ddd.domain.DDD;
import com.questmast.questmast.core.contact.ddi.domain.DDI;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PhoneRepository extends JpaRepository<Phone, String> {
    boolean existsByNumberAndDddAndDdi(String number, DDD ddd, DDI ddi);
}
