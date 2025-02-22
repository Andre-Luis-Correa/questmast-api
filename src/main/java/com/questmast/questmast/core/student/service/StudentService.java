package com.questmast.questmast.core.student.service;

import com.questmast.questmast.common.exception.type.EntityNotFoundExcpetion;
import com.questmast.questmast.core.address.address.domain.entity.Address;
import com.questmast.questmast.core.address.address.domain.entity.SpecificAddress;
import com.questmast.questmast.core.authentication.user.domain.dto.UserFormDTO;
import com.questmast.questmast.core.contact.phone.domain.model.Phone;
import com.questmast.questmast.core.gender.domain.Gender;
import com.questmast.questmast.core.person.cpf.domain.CPF;
import com.questmast.questmast.core.student.domain.Student;
import com.questmast.questmast.core.student.mapper.StudentMapper;
import com.questmast.questmast.core.student.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public void create(UserFormDTO userFormDTO, CPF cpf, Gender gender, Address address, String mainEmail, List<Phone> phoneList) {
        SpecificAddress specificAddress = new SpecificAddress(userFormDTO.specificAddressFormDTO().number(), userFormDTO.specificAddressFormDTO().complement(), address);
        Student student = convertToStudent(userFormDTO, cpf, gender, specificAddress, mainEmail, phoneList);

        studentRepository.save(student);
    }

    private Student convertToStudent(UserFormDTO userFormDTO, CPF cpf, Gender gender, SpecificAddress specificAddress, String mainEmail, List<Phone> phoneList) {
        return studentMapper.convertToStudent(userFormDTO, cpf, gender, specificAddress, mainEmail, phoneList);
    }

    public Student findByMainEmail(String email) {
        return studentRepository.findByMainEmail(email).orElseThrow(
                () -> new EntityNotFoundExcpetion("Student", "mainEmail", email)
        );
    }

    public Student findByRecoveryEmail(String email) {
        return studentRepository.findByRecoveryEmail(email).orElseThrow(
                () -> new EntityNotFoundExcpetion("Student", "recoveryEmail", email)
        );
    }
}
