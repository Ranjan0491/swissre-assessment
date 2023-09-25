package com.swissre.app.services;

import com.swissre.app.dto.CreateTaskRequestDto;
import com.swissre.app.dto.TaskResponseDto;
import com.swissre.app.dto.UpdateTaskRequestDto;
import com.swissre.app.entities.TaskEntity;
import com.swissre.app.enums.ErrorDetail;
import com.swissre.app.enums.SortColumn;
import com.swissre.app.enums.TaskPriority;
import com.swissre.app.exceptions.ResourceNotFoundException;
import com.swissre.app.mappers.TaskMapper;
import com.swissre.app.repositories.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskResponseDto saveTask(CreateTaskRequestDto taskDto) {
        var taskEntity = taskMapper.toEntity(taskDto);
        var savedTaskEntity = taskRepository.saveAndFlush(taskEntity);

        return taskMapper.toDto(savedTaskEntity);
    }

    @Transactional
    public TaskResponseDto updateTask(UpdateTaskRequestDto taskDto, int id) {
        var existingTaskEntity = findTaskEntityById(id);
        taskMapper.merge(existingTaskEntity, taskMapper.toEntity(taskDto));
        var updatedTaskEntity = taskRepository.saveAndFlush(existingTaskEntity);

        return taskMapper.toDto(updatedTaskEntity);
    }

    public TaskResponseDto getTaskById(int id) {
        return taskMapper.toDto(findTaskEntityById(id));
    }

    public void deleteTaskById(int id) {
        var existingTaskEntity = findTaskEntityById(id);
        taskRepository.delete(existingTaskEntity);
    }

    public List<TaskResponseDto> getTasks(TaskPriority priority, Boolean completed, SortColumn sortColumn, Sort.Direction direction) {
        List<TaskEntity> tasks;
        Sort sort = Sort.unsorted();
        if(sortColumn != null && direction != null) {
            sort = Sort.by(direction, sortColumn.getColumnName());
        }
        if (priority == null && completed == null) {
            tasks = taskRepository.findAll(sort);
        } else if (priority != null && completed == null) {
            tasks = taskRepository.findByPriority(priority, sort);
        } else if (priority == null) {
            tasks = taskRepository.findByCompleted(completed, sort);
        } else {
            tasks = taskRepository.findByPriorityAndCompleted(priority, completed, sort);
        }

        return CollectionUtils.isEmpty(tasks) ? null : tasks.stream()
                    .map(taskMapper::toDto)
                    .collect(Collectors.toList());

    }

    private TaskEntity findTaskEntityById(int id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorDetail.TASK_NOT_FOUND,
                        Map.ofEntries(Map.entry("taskId", String.valueOf(id)))));
    }
}
