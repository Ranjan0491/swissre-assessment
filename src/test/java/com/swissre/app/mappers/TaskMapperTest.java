package com.swissre.app.mappers;

import com.swissre.app.dto.CreateTaskRequestDto;
import com.swissre.app.dto.UpdateTaskRequestDto;
import com.swissre.app.entities.TaskEntity;
import com.swissre.app.enums.TaskPriority;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Mapper tests")
class TaskMapperTest {

    @InjectMocks
    private TaskMapper mapper = new TaskMapperImpl();

    @Test
    @DisplayName("Test for toDto()")
    void toDto() {
        // given
        var taskEntity = new TaskEntity();
        taskEntity.setCompleted(true);
        taskEntity.setDescription("Task description");
        taskEntity.setPriority(TaskPriority.HIGH);
        taskEntity.setId(1);

        // when
        var taskDto = mapper.toDto(taskEntity);

        // then
        assertAll(
                () -> assertEquals(taskEntity.getId(), taskDto.getId()),
                () -> assertEquals(taskEntity.getDescription(), taskDto.getDescription()),
                () -> assertEquals(taskEntity.isCompleted(), taskDto.isCompleted()),
                () -> assertEquals(taskEntity.getPriority(), taskDto.getPriority())
        );
    }

    @Test
    @DisplayName("Test for toEntity() from CreateTaskRequestDto")
    void toEntity_CreateTaskRequestDto() {
        // given
        var taskDto = new CreateTaskRequestDto();
        taskDto.setCompleted(true);
        taskDto.setDescription("Task description");
        taskDto.setPriority(TaskPriority.HIGH);

        // when
        var taskEntity = mapper.toEntity(taskDto);

        // then
        assertAll(
                () -> assertEquals(taskDto.getDescription(), taskEntity.getDescription()),
                () -> assertEquals(taskDto.isCompleted(), taskEntity.isCompleted()),
                () -> assertEquals(taskDto.getPriority(), taskEntity.getPriority())
        );
    }

    @Test
    @DisplayName("Test for toEntity() from UpdateTaskRequestDto")
    void toEntity_UpdateTaskRequestDto() {
        // given
        var taskDto = new UpdateTaskRequestDto();
        taskDto.setCompleted(true);
        taskDto.setDescription("Task description");
        taskDto.setPriority(TaskPriority.HIGH);

        // when
        var taskEntity = mapper.toEntity(taskDto);

        // then
        assertAll(
                () -> assertEquals(taskDto.getDescription(), taskEntity.getDescription()),
                () -> assertEquals(taskDto.isCompleted(), taskEntity.isCompleted()),
                () -> assertEquals(taskDto.getPriority(), taskEntity.getPriority())
        );
    }

    @Test
    @DisplayName("Test for merge()")
    void merge() {
        // given
        var oldTaskEntity = new TaskEntity();
        oldTaskEntity.setCompleted(true);
        oldTaskEntity.setDescription("Task description");
        oldTaskEntity.setPriority(TaskPriority.HIGH);
        oldTaskEntity.setId(1);

        var newTaskEntity = new TaskEntity();
        newTaskEntity.setCompleted(false);
        newTaskEntity.setDescription("Task description");
        newTaskEntity.setPriority(TaskPriority.MEDIUM);
        newTaskEntity.setId(1);

        // when
        mapper.merge(oldTaskEntity, newTaskEntity);

        // then
        assertAll(
                () -> assertEquals(newTaskEntity.getId(), oldTaskEntity.getId()),
                () -> assertEquals(newTaskEntity.getDescription(), oldTaskEntity.getDescription()),
                () -> assertEquals(newTaskEntity.isCompleted(), oldTaskEntity.isCompleted()),
                () -> assertEquals(newTaskEntity.getPriority(), oldTaskEntity.getPriority())
        );
    }
}