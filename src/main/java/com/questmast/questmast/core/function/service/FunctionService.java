package com.questmast.questmast.core.function.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.function.Function;
import com.questmast.questmast.core.function.repository.FunctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FunctionService {

    private final FunctionRepository functionRepository;

    public Function findById(Long id) {
        return functionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("Function", "id", id.toString())
        );
    }
}
