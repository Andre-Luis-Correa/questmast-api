package com.questmast.questmast.core.selectionprocess.domain.model;

import com.questmast.questmast.core.address.city.domain.City;
import com.questmast.questmast.core.boardexaminer.domain.BoardExaminer;
import com.questmast.questmast.core.contentmoderator.domain.ContentModerator;
import com.questmast.questmast.core.selectionprocessstatus.domain.entity.SelectionProcessStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class SelectionProcess {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Integer viewCounter;

    private String url;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "city_id ", nullable = false)
    private City city;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "board_examiner_id ", nullable = false)
    private BoardExaminer boardExaminer;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "content_moderator_id", nullable = false)
    private ContentModerator contentModerator;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "selection_process_status_id", nullable = false)
    private SelectionProcessStatus selectionProcessStatus;
}
