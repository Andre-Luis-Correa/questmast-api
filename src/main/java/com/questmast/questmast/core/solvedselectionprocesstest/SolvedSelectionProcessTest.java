package com.questmast.questmast.core.solvedselectionprocesstest;

import com.questmast.questmast.core.questionnaire.Questionnaire;
import com.questmast.questmast.core.selectionprocesstest.SelectionProcessTest;
import com.questmast.questmast.core.solvedevaluationtest.SolvedEvaluationTest;
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
public class SolvedSelectionProcessTest extends SolvedEvaluationTest {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "selection_process_test_id", nullable = false)
    private SelectionProcessTest selectionProcessTest;
}
