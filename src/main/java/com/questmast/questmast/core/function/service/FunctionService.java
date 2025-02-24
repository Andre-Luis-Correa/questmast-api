package com.questmast.questmast.core.function.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.function.domain.dto.FunctionFormDTO;
import com.questmast.questmast.core.function.domain.model.Function;
import com.questmast.questmast.core.function.repository.FunctionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FunctionService {

    private final FunctionRepository functionRepository;

    public Function findById(Long id) {
        return functionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("Function", "id", id.toString())
        );
    }

    public void create(FunctionFormDTO functionFormDTO) {
        Function function = new Function();
        function.setName(functionFormDTO.name());
        function.setDescription(functionFormDTO.description());

        functionRepository.save(function);
    }

    public void update(Function function, FunctionFormDTO functionFormDTO) {
        function.setName(functionFormDTO.name());
        function.setDescription(functionFormDTO.description());

        functionRepository.save(function);
    }

    public List<Function> findAll() {
        return functionRepository.findAll();
    }

    public Page<Function> findAll(Pageable pageable) {
        return functionRepository.findAll(pageable);
    }

    public void delete(Function function) {
        functionRepository.delete(function);
    }
}
