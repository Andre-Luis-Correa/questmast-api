package com.questmast.questmast.core.selectionprocesstest.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.function.domain.model.Function;
import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;
import com.questmast.questmast.core.question.domain.model.Question;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.selectionprocesstest.domain.dto.SelectionProcessTestFormDTO;
import com.questmast.questmast.core.selectionprocesstest.domain.model.SelectionProcessTest;
import com.questmast.questmast.core.selectionprocesstest.mapper.SelectionProcessTestMapper;
import com.questmast.questmast.core.selectionprocesstest.repository.SelectionProcessTestRepository;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class SelectionProcessTestService {

    private final SelectionProcessTestRepository selectionProcessTestRepository;
    private final SelectionProcessTestMapper selectionProcessTestMapper;

    public SelectionProcessTest findById(Long id) {
        return selectionProcessTestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("SelectionProcessTest", "id", id.toString())
        );
    }

    public void create(SelectionProcessTestFormDTO selectionProcessTestFormDTO, Function function, ProfessionalLevel professionalLevel, TestQuestionCategory testQuestionCategory, SelectionProcess selectionProcess, List<Question> questionList) {
        SelectionProcessTest selectionProcessTest = new SelectionProcessTest();
        selectionProcessTest.setApplicationDate(selectionProcessTestFormDTO.applicationDate());
        selectionProcessTest.setName(selectionProcessTestFormDTO.name());
        selectionProcessTest.setViewCounter(0);
        selectionProcessTest.setQuestionList(questionList);
        selectionProcessTest.setFunction(function);
        selectionProcessTest.setProfessionalLevel(professionalLevel);
        selectionProcessTest.setTestQuestionCategory(testQuestionCategory);
        selectionProcessTest.setSelectionProcess(selectionProcess);

        selectionProcessTestRepository.save(selectionProcessTest);
    }

    public void update(SelectionProcessTestFormDTO selectionProcessTestUpdateDTO, SelectionProcessTest selectionProcessTest, Function function, ProfessionalLevel professionalLevel, TestQuestionCategory testQuestionCategory, SelectionProcess selectionProcess, List<Question> questionList) {
        selectionProcessTest.setName(selectionProcessTestUpdateDTO.name());
        selectionProcessTest.setApplicationDate(selectionProcessTestUpdateDTO.applicationDate());
        selectionProcessTest.setQuestionList(questionList);
        selectionProcessTest.setFunction(function);
        selectionProcessTest.setProfessionalLevel(professionalLevel);
        selectionProcessTest.setTestQuestionCategory(testQuestionCategory);
        selectionProcessTest.setSelectionProcess(selectionProcess);

        selectionProcessTestRepository.save(selectionProcessTest);
    }

    public void updateViewCounter(SelectionProcessTest selectionProcessTest) {
        Integer viewCounter  = selectionProcessTest.getViewCounter();

        if(viewCounter == null) {
            viewCounter = 0;
        } else {
            viewCounter += 1;
        }

        selectionProcessTest.setViewCounter(viewCounter);

        selectionProcessTestRepository.save(selectionProcessTest);
    }

    public void delete(SelectionProcessTest selectionProcessTest) {
        selectionProcessTestRepository.delete(selectionProcessTest);
    }

    public Page<SelectionProcessTest> list(Pageable pageable) {
        return selectionProcessTestRepository.findAll(pageable);
    }

    public List<SelectionProcessTest> list() {
        return selectionProcessTestRepository.findAll();
    }
}
