package com.swissre.app.controller;

import com.swissre.app.dto.CreateTaskRequestDto;
import com.swissre.app.dto.TaskResponseDto;
import com.swissre.app.dto.UpdateTaskRequestDto;
import com.swissre.app.enums.EnumUtils;
import com.swissre.app.enums.SortColumn;
import com.swissre.app.enums.TaskPriority;
import com.swissre.app.services.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponseDto> saveTask(@RequestBody @Valid CreateTaskRequestDto taskDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.saveTask(taskDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDto> updateTask(@PathVariable(name = "id") int id, @RequestBody @Valid UpdateTaskRequestDto taskDto) {
        return ResponseEntity.ok(taskService.updateTask(taskDto, id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOneTask(@PathVariable(name = "id") int id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponseDto> getOneTask(@PathVariable(name = "id") int id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDto>> getTasks(@RequestParam(name = "priority", required = false) String priority,
                                                  @RequestParam(name = "completed", required = false) Boolean completed,
                                                  @RequestParam(name = "columnName", required = false) String columnName,
                                                  @RequestParam(name = "sortDirection", required = false) Sort.Direction direction) {
        return ResponseEntity.ok(
                taskService.getTasks(
                        EnumUtils.convertAndGetEnum(priority, TaskPriority.values()),
                        completed,
                        EnumUtils.convertAndGetEnum(columnName, SortColumn.values()),
                        direction)
        );
    }
}
