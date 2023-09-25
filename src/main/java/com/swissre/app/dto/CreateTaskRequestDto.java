package com.swissre.app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.swissre.app.enums.TaskPriority;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateTaskRequestDto {
    @NotNull
    private String description;
    private boolean completed;
    @NotNull
    private TaskPriority priority;
    private List<SubTaskCommonDto> subTasks;
}
