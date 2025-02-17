package com.questmast.questmast.core.selectionprocesstest;

import com.questmast.questmast.core.evaluationtest.EvaluationTest;
import com.questmast.questmast.core.function.Function;
import com.questmast.questmast.core.professionallevel.ProfessionalLevel;
import com.questmast.questmast.core.selectionprocess.SelectionProcess;
import com.questmast.questmast.core.testquestioncategory.TestQuestionCategory;
import jakarta.persistence.*;
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
public class SelectionProcessTest extends EvaluationTest {

    @NotNull
    @ManyToOne
    @JoinColumn(name = "function_id", nullable = false)
    private Function function;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "professional_level_id", nullable = false)
    private ProfessionalLevel professionalLevel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "test_question_category_id", nullable = false)
    private TestQuestionCategory testQuestionCategory;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "selection_process_id", nullable = false)
    private SelectionProcess selectionProcess;
}
