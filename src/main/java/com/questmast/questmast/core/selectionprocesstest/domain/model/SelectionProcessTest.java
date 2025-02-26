package com.questmast.questmast.core.selectionprocesstest.domain.model;

import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.evaluationtest.EvaluationTest;
import com.questmast.questmast.core.function.domain.model.Function;
import com.questmast.questmast.core.professionallevel.domain.entity.ProfessionalLevel;
import com.questmast.questmast.core.selectionprocess.domain.model.SelectionProcess;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class SelectionProcessTest extends EvaluationTest {

    @NotNull
    @Column(nullable = false)
    private LocalDate applicationDate;

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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "content_moderator_id", nullable = false)
    private ContentModerator contentModerator;
}
