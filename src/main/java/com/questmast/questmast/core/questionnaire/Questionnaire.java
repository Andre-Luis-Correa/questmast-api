package com.questmast.questmast.core.questionnaire;

import com.questmast.questmast.core.evaluationtest.EvaluationTest;
import com.questmast.questmast.core.student.domain.Student;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Questionnaire extends EvaluationTest {

    @NotNull
    @Column(nullable = false)
    private Boolean isPublic;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;
}
