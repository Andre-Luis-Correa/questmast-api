package com.questmast.questmast.core.contact.ddd.service;

import com.questmast.questmast.common.exception.domain.EntityNotFoundExcpetion;
import com.questmast.questmast.core.contact.ddd.domain.DDD;
import com.questmast.questmast.core.contact.ddd.repository.DDDRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DDDService {

    private final DDDRepository dddRepository;

    public DDD findByDdd(Integer ddd) {
        return dddRepository.findByDdd(ddd).orElseThrow(
                () -> new EntityNotFoundExcpetion("DDD", "ddd", ddd.toString())
        );
    }
}
