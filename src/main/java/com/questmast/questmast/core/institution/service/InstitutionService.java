package com.questmast.questmast.core.institution.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.institution.domain.dto.InstitutionFormDTO;
import com.questmast.questmast.core.institution.domain.model.Institution;
import com.questmast.questmast.core.institution.repository.InstitutionRepository;
import com.questmast.questmast.core.person.cnpj.CNPJ;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void create(InstitutionFormDTO institutionFormDTO) {
        Institution institution = new Institution();
        institution.setName(institutionFormDTO.name());
        institution.setCnpj(new CNPJ(institutionFormDTO.cnpj()));
        institution.setSiteUrl(institutionFormDTO.siteUrl());
        institution.setQuantityOfTests(0);
        institution.setQuantityOfSelectionProcess(0);
        institution.setQuantityOfQuestions(0);

        institutionRepository.save(institution);
    }

    public void update(Institution institution, InstitutionFormDTO institutionFormDTO) {
        institution.setName(institutionFormDTO.name());
        institution.setCnpj(new CNPJ(institutionFormDTO.cnpj()));
        institution.setSiteUrl(institutionFormDTO.siteUrl());

        institutionRepository.save(institution);
    }

    public void delete(Institution institution) {
        institutionRepository.delete(institution);
    }

    public List<Institution> findAll() {
        return institutionRepository.findAll();
    }

    public Page<Institution> findAll(Pageable pageable) {
        return institutionRepository.findAll(pageable);
    }
}
