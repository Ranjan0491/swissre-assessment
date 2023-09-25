package com.swissre.app.entities;

import com.swissre.app.enums.TaskPriority;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class TaskEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String description;
    private boolean completed;
    private TaskPriority priority;
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubTaskEntity> subTasks;
}
