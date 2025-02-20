package com.questmast.questmast.core.contact.phone.service;

import com.questmast.questmast.common.exception.domain.InvalidContactException;
import com.questmast.questmast.core.contact.ddd.domain.DDD;
import com.questmast.questmast.core.contact.ddd.service.DDDService;
import com.questmast.questmast.core.contact.ddi.domain.DDI;
import com.questmast.questmast.core.contact.ddi.service.DDIService;
import com.questmast.questmast.core.contact.phone.domain.dto.PhoneFormDTO;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.contact.phone.repository.PhoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PhoneService {

    private final PhoneRepository phoneRepository;
    private final DDDService dddService;
    private final DDIService ddiService;


    public List<Phone> generateValidPhoneList(List<PhoneFormDTO> phoneFormDTOS) {
        List<Phone> phoneList = new ArrayList<>();

        for(PhoneFormDTO phoneFormDTO : phoneFormDTOS) {
            DDD ddd = dddService.findByDdd(phoneFormDTO.dddNumber());
            DDI ddi = ddiService.findByDdi(phoneFormDTO.ddiNumber());

            if(phoneRepository.existsByNumberAndDddAndDdi(phoneFormDTO.number(), ddd, ddi)) {
                throw new InvalidContactException("Phone", "+ " + ddi.getDdi() + " " + ddd.getDdd() + " " + phoneFormDTO.number());
            }

            Phone phone = new Phone(phoneFormDTO.number(), ddd, ddi);
            phoneList.add(phone);
        }
        return phoneList;
    }
}
