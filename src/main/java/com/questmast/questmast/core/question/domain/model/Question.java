package com.questmast.questmast.core.question.domain.model;

import com.questmast.questmast.core.questionalternative.domain.entity.QuestionAlternative;
import com.questmast.questmast.core.questiondifficultylevel.domain.entity.QuestionDifficultyLevel;
import com.questmast.questmast.core.subject.domain.entity.Subject;
import com.questmast.questmast.core.subjecttopic.domain.entity.SubjectTopic;
import com.questmast.questmast.core.testquestioncategory.domain.entity.TestQuestionCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDate applicationDate;

    @NotNull
    @Column(nullable = false)
    private String name;

    private String statementImageUrl;
    private String statementImageLegend;

    @NotNull
    @Column(nullable = false)
    private String statement;

    @NotNull
    @Column(nullable = false)
    private Integer quantityOfCorrectAnswers;

    @NotNull
    @Column(nullable = false)
    private Integer quantityOfWrongAnswers;

    @NotNull
    @Column(nullable = false)
    private Integer quantityOfTries;

    @NotNull
    @Column(nullable = false)
    private String explanation;

    private String videoExplanationUrl;

    @NotEmpty
    @OneToMany(cascade = CascadeType.ALL)
    private List<QuestionAlternative> questionAlternativeList;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "question_difficulty_level_id", nullable = false)
    private QuestionDifficultyLevel questionDifficultyLevel;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    @NotEmpty
    @OneToMany
    private Set<SubjectTopic> subjectTopicList;
}
