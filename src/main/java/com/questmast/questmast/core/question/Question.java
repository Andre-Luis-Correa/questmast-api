package com.questmast.questmast.core.question;

import com.questmast.questmast.core.questionalternative.QuestionAlternative;
import com.questmast.questmast.core.questiondifficultylevel.QuestionDifficultyLevel;
import com.questmast.questmast.core.subject.Subject;
import com.questmast.questmast.core.subjecttopic.SubjectTopic;
import com.questmast.questmast.core.testquestioncategory.TestQuestionCategory;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

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

    @NotNull
    @Column(nullable = false)
    private String videoExplanationUrl;

    @NotNull
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

    @NotNull
    @ManyToOne
    @JoinColumn(name = "test_question_category_id", nullable = false)
    private TestQuestionCategory testQuestionCategory;

    @NotNull
    @OneToMany
    private List<SubjectTopic> subjectTopicList;
}
