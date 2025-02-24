package com.questmast.questmast.core.institution.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.institution.domain.model.Institution;
import com.questmast.questmast.core.institution.repository.InstitutionRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InstitutionService {

    private final InstitutionRepository institutionRepository;

    public Institution findById(Long id) {
        return institutionRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundExcpetion("Institution", "id", id.toString())
        );
    }

    public void addTestsAndQuestionsCounters(Institution institution, int numberOfQuestions) {
        institution.setQuantityOfTests(institution.getQuantityOfTests() + 1);
        institution.setQuantityOfQuestions(institution.getQuantityOfQuestions() + numberOfQuestions);
        institutionRepository.save(institution);
    }

    public void subTestsAndQuestionsCounters(Institution institution, int numberOfQuestions) {
        if(institution.getQuantityOfTests() != 0) {
            institution.setQuantityOfTests(institution.getQuantityOfTests() - 1);
        }
        if(institution.getQuantityOfQuestions() != 0) {
            institution.setQuantityOfQuestions(institution.getQuantityOfQuestions() - numberOfQuestions);
        }

        institutionRepository.save(institution);
    }

    public void addQuantityOfSelectionProcess(Institution institution) {
        institution.setQuantityOfSelectionProcess(institution.getQuantityOfSelectionProcess() + 1);
        institutionRepository.save(institution);
    }

    public void subQuantityOfSelectionProcess(Institution institution) {
        if(institution.getQuantityOfSelectionProcess() > 0) {
            institution.setQuantityOfSelectionProcess(institution.getQuantityOfSelectionProcess() - 1);
        }

        institutionRepository.save(institution);
    }
}
