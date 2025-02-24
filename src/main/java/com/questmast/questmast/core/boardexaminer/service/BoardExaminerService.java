package com.questmast.questmast.core.boardexaminer.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.boardexaminer.domain.BoardExaminer;
import com.questmast.questmast.core.boardexaminer.repository.BoardExaminerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardExaminerService {

    private final BoardExaminerRepository boardExaminerRepository;

    public BoardExaminer findById(Long id) {
        return boardExaminerRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("BoardExaminer", "id", id.toString())
        );
    }
}
