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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SelectionProcessTestService {

    private final SelectionProcessTestRepository selectionProcessRepository;
    private final SelectionProcessTestMapper selectionProcessTestMapper;

    public SelectionProcessTest findById(Long id) {
        return selectionProcessRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("SelectionProcessTest", "id", id.toString())
        );
    }

    public void create(SelectionProcessTestFormDTO selectionProcessTestFormDTO, Function function, ProfessionalLevel professionalLevel, TestQuestionCategory testQuestionCategory, SelectionProcess selectionProcess, List<Question> questionList) {
        SelectionProcessTest selectionProcessTest = new SelectionProcessTest();
        selectionProcessTest.setName(selectionProcessTestFormDTO.name());
        selectionProcessTest.setViewCounter(0);
        selectionProcessTest.setQuestionList(questionList);
        selectionProcessTest.setFunction(function);
        selectionProcessTest.setProfessionalLevel(professionalLevel);
        selectionProcessTest.setTestQuestionCategory(testQuestionCategory);
        selectionProcessTest.setSelectionProcess(selectionProcess);

        selectionProcessRepository.save(selectionProcessTest);
    }
}
