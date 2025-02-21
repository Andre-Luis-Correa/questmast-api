package com.questmast.questmast.core.contact.ddi.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.contact.ddi.domain.DDI;
import com.questmast.questmast.core.contact.ddi.repository.DDIRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DDIService {

    private final DDIRepository ddiRepository;

    public DDI findByDdi(Integer ddi) {
        return ddiRepository.findByDdi(ddi).orElseThrow(
                () -> new EntityNotFoundExcpetion("DDI", "ddi", ddi.toString())
        );
    }
}
