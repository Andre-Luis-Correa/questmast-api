package com.questmast.questmast.core.boardexaminer.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.boardexaminer.domain.BoardExaminer;
import com.questmast.questmast.core.boardexaminer.repository.BoardExaminerRepository;
import jakarta.validation.constraints.NotNull;
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

    public void addQuantityOfSelectionProcess(BoardExaminer boardExaminer) {
        boardExaminer.setQuantityOfSelectionProcess(boardExaminer.getQuantityOfSelectionProcess() + 1);
        boardExaminerRepository.save(boardExaminer);
    }

    public void addTestsAndQuestionsCounters(BoardExaminer boardExaminer, int numberOfQuestions) {
        boardExaminer.setQuantityOfTests(boardExaminer.getQuantityOfTests() + 1);
        boardExaminer.setQuantityOfQuestions(boardExaminer.getQuantityOfQuestions() + numberOfQuestions);
        boardExaminerRepository.save(boardExaminer);
    }

    public void subTestsAndQuestionsCounters(BoardExaminer boardExaminer, int numberOfQuestions) {
        if(boardExaminer.getQuantityOfTests() != 0) {
            boardExaminer.setQuantityOfTests(boardExaminer.getQuantityOfTests() - 1);
        }
        if(boardExaminer.getQuantityOfQuestions() != 0) {
            boardExaminer.setQuantityOfQuestions(boardExaminer.getQuantityOfQuestions() - numberOfQuestions);
        }

        boardExaminerRepository.save(boardExaminer);
    }
}
