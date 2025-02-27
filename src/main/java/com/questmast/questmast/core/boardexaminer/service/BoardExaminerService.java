package com.questmast.questmast.core.boardexaminer.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.boardexaminer.domain.dto.BoardExaminerFormDTO;
import com.questmast.questmast.core.boardexaminer.domain.model.BoardExaminer;
import com.questmast.questmast.core.boardexaminer.repository.BoardExaminerRepository;
import com.questmast.questmast.core.person.cnpj.CNPJ;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void create(BoardExaminerFormDTO boardExaminerFormDTO) {
        BoardExaminer boardExaminer = new BoardExaminer();
        boardExaminer.setName(boardExaminerFormDTO.name());
        boardExaminer.setSiteUrl(boardExaminerFormDTO.siteUrl());
        boardExaminer.setCnpj(new CNPJ(boardExaminerFormDTO.cnpj()));
        boardExaminer.setQuantityOfTests(0);
        boardExaminer.setQuantityOfSelectionProcess(0);
        boardExaminer.setQuantityOfQuestions(0);

        boardExaminerRepository.save(boardExaminer);
    }

    public void update(BoardExaminer boardExaminer, BoardExaminerFormDTO boardExaminerFormDTO) {
        boardExaminer.setName(boardExaminerFormDTO.name());
        boardExaminer.setSiteUrl(boardExaminerFormDTO.siteUrl());
        boardExaminer.setCnpj(new CNPJ(boardExaminerFormDTO.cnpj()));
        boardExaminer.setQuantityOfTests(boardExaminer.getQuantityOfTests());
        boardExaminer.setQuantityOfSelectionProcess(boardExaminer.getQuantityOfSelectionProcess());
        boardExaminer.setQuantityOfQuestions(boardExaminer.getQuantityOfQuestions());

        boardExaminerRepository.save(boardExaminer);
    }

    public void delete(BoardExaminer boardExaminer) {
        boardExaminerRepository.delete(boardExaminer);
    }

    public List<BoardExaminer> findAll() {
        return boardExaminerRepository.findAll();
    }

    public Page<BoardExaminer> findAll(Pageable pageable) {
        return boardExaminerRepository.findAll(pageable);
    }
}
