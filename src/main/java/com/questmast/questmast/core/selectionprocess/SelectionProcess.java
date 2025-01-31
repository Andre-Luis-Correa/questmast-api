package com.questmast.questmast.core.selectionprocess;

import com.questmast.questmast.core.selectionprocessstatus.SelectionProcessStatus;
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
    @ManyToOne
    @JoinColumn(name = "selection_process_status_id", nullable = false)
    private SelectionProcessStatus selectionProcessStatus;
}
