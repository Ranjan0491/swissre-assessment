package com.swissre.app.dto;

import com.swissre.app.enums.TaskPriority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class TaskResponseDto {
    private int id;
    private String description;
    private boolean completed;
    private TaskPriority priority;
    private List<SubTaskDto> subTasks;
}
