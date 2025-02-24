package com.questmast.questmast.core.selectionprocesstest.service;

import com.questmast.questmast.core.selectionprocesstest.mapper.SelectionProcessTestMapper;
import com.questmast.questmast.core.selectionprocesstest.repository.SelectionProcessTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SelectionProcessTestService {

    private final SelectionProcessTestRepository selectionProcessRepository;
    private final SelectionProcessTestMapper selectionProcessTestMapper;
}
