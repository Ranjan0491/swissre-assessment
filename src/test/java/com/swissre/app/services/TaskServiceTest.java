package com.swissre.app.services;

import com.swissre.app.dto.*;
import com.swissre.app.entities.SubTaskEntity;
import com.swissre.app.entities.TaskEntity;
import com.swissre.app.enums.SortColumn;
import com.swissre.app.enums.TaskPriority;
import com.swissre.app.exceptions.ResourceNotFoundException;
import com.swissre.app.mappers.TaskMapper;
import com.swissre.app.mappers.TaskMapperImpl;
import com.swissre.app.repositories.TaskRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service layer tests")
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Spy
    private TaskMapper taskMapper = new TaskMapperImpl();
    @InjectMocks
    private TaskService taskService;
    @Captor
    ArgumentCaptor<List<SubTaskEntity>> subTaskEntityListCaptor;
    @Captor
    ArgumentCaptor<TaskEntity> taskEntityCaptor;

    @Nested
    @DisplayName("Tests for create task")
    class TestCreateTask {
        @Test
        @DisplayName("Test for Save new Task")
        void saveTask() {
            // given
            var subTask1Dto = new SubTaskCommonDto();
            subTask1Dto.setCompleted(true);
            subTask1Dto.setDescription("Sub Task 1 description");

            var subTask2Dto = new SubTaskCommonDto();
            subTask2Dto.setCompleted(false);
            subTask2Dto.setDescription("Sub Task 2 description");

            var taskDto = new CreateTaskRequestDto();
            taskDto.setDescription("Task description");
            taskDto.setCompleted(true);
            taskDto.setPriority(TaskPriority.HIGH);
            taskDto.setSubTasks(List.of(subTask1Dto, subTask2Dto));

            var subTask1Entity = new SubTaskEntity();
            subTask1Entity.setCompleted(true);
            subTask1Entity.setDescription("Sub Task 1 description");
            subTask1Entity.setId(1);

            var subTask2Entity = new SubTaskEntity();
            subTask2Entity.setCompleted(false);
            subTask2Entity.setDescription("Sub Task 2 description");
            subTask2Entity.setId(2);

            List<SubTaskEntity> subTaskEntityList = new ArrayList<>();
            subTaskEntityList.add(subTask1Entity);
            subTaskEntityList.add(subTask2Entity);
            var taskEntity = taskMapper.toEntity(taskDto);

            // when
            when(taskRepository.saveAndFlush(any())).thenReturn(taskEntity);

            var savedTaskDto = taskService.saveTask(taskDto);

            // then
            assertNotNull(savedTaskDto);
            assertAll(
                    () -> assertEquals(taskDto.getDescription(), savedTaskDto.getDescription()),
                    () -> assertEquals(taskDto.isCompleted(), savedTaskDto.isCompleted()),
                    () -> assertEquals(taskDto.getPriority(), savedTaskDto.getPriority())
            );
            verify(taskRepository).saveAndFlush(taskEntityCaptor.capture());
        }
    }

    @Nested
    @DisplayName("Tests for update task")
    class TestUpdateTask {
        @Test
        @DisplayName("Test for Update an existing Task")
        void updateTask() {
            // given
            int id = 1;
            var subTask1Entity = new SubTaskEntity();
            subTask1Entity.setCompleted(true);
            subTask1Entity.setDescription("Sub Task 1 description");
            subTask1Entity.setId(1);

            var subTask2Entity = new SubTaskEntity();
            subTask2Entity.setCompleted(false);
            subTask2Entity.setDescription("Sub Task 2 description");
            subTask2Entity.setId(2);

            List<SubTaskEntity> subTaskEntityList = new ArrayList<>();
            subTaskEntityList.add(subTask1Entity);
            subTaskEntityList.add(subTask2Entity);

            var taskEntity = new TaskEntity();
            taskEntity.setPriority(TaskPriority.HIGH);
            taskEntity.setCompleted(false);
            taskEntity.setDescription("Task description");
            taskEntity.setId(id);
            taskEntity.setSubTasks(subTaskEntityList);

            var subTask1Dto = new SubTaskDto();
            subTask1Dto.setCompleted(true);
            subTask1Dto.setDescription("Sub Task 1 description");
            subTask1Dto.setId(1);

            var subTask2Dto = new SubTaskDto();
            subTask2Dto.setCompleted(false);
            subTask2Dto.setDescription("Sub Task 2 description");
            subTask2Dto.setId(2);

            var taskDto = new UpdateTaskRequestDto();
            taskDto.setDescription("Updated Task description");
            taskDto.setCompleted(true);
            taskDto.setPriority(TaskPriority.HIGH);
            taskDto.setSubTasks(List.of(subTask1Dto, subTask2Dto));

            // when
            when(taskRepository.findById(any())).thenReturn(Optional.of(taskEntity));
            when(taskRepository.saveAndFlush(any())).thenReturn(taskEntity);

            var updatedTaskDto = taskService.updateTask(taskDto, id);

            // then
            assertNotNull(updatedTaskDto);
            assertAll(
                    () -> assertEquals(taskDto.getDescription(), updatedTaskDto.getDescription()),
                    () -> assertEquals(taskDto.isCompleted(), updatedTaskDto.isCompleted()),
                    () -> assertEquals(taskDto.getPriority(), updatedTaskDto.getPriority())
            );
            verify(taskRepository).findById(id);
            verify(taskRepository).saveAndFlush(taskEntityCaptor.capture());
        }

        @Test
        @DisplayName("Test for Update a non existing Task")
        void updateTaskForNonExistingTask() {
            // given
            int id = 1;
            var subTask1Entity = new SubTaskEntity();
            subTask1Entity.setCompleted(true);
            subTask1Entity.setDescription("Sub Task 1 description");
            subTask1Entity.setId(1);

            var subTask2Entity = new SubTaskEntity();
            subTask2Entity.setCompleted(false);
            subTask2Entity.setDescription("Sub Task 2 description");
            subTask2Entity.setId(2);

            List<SubTaskEntity> subTaskEntityList = new ArrayList<>();
            subTaskEntityList.add(subTask1Entity);
            subTaskEntityList.add(subTask2Entity);

            var taskEntity = new TaskEntity();
            taskEntity.setPriority(TaskPriority.HIGH);
            taskEntity.setCompleted(false);
            taskEntity.setDescription("Task description");
            taskEntity.setId(id);
            taskEntity.setSubTasks(subTaskEntityList);

            var subTask1Dto = new SubTaskDto();
            subTask1Dto.setCompleted(true);
            subTask1Dto.setDescription("Sub Task 1 description");
            subTask1Dto.setId(1);

            var subTask2Dto = new SubTaskDto();
            subTask2Dto.setCompleted(false);
            subTask2Dto.setDescription("Sub Task 2 description");
            subTask2Dto.setId(2);

            var taskDto = new UpdateTaskRequestDto();
            taskDto.setDescription("Updated Task description");
            taskDto.setCompleted(true);
            taskDto.setPriority(TaskPriority.HIGH);
            taskDto.setSubTasks(List.of(subTask1Dto, subTask2Dto));

            // when
            when(taskRepository.findById(anyInt())).thenThrow(ResourceNotFoundException.class);

            Executable executable = () -> taskService.updateTask(taskDto, id);

            // then
            assertThrows(ResourceNotFoundException.class, executable);
            verify(taskRepository).findById(id);
        }
    }

    @Nested
    @DisplayName("Tests for get task by id")
    class TestGetTaskById {
        @Test
        @DisplayName("Test for Get task by id")
        void getTaskById() {
            // given
            int id = 1;
            var subTask1Entity = new SubTaskEntity();
            subTask1Entity.setCompleted(true);
            subTask1Entity.setDescription("Sub Task 1 description");
            subTask1Entity.setId(1);

            var subTask2Entity = new SubTaskEntity();
            subTask2Entity.setCompleted(false);
            subTask2Entity.setDescription("Sub Task 2 description");
            subTask2Entity.setId(2);

            List<SubTaskEntity> subTaskEntityList = new ArrayList<>();
            subTaskEntityList.add(subTask1Entity);
            subTaskEntityList.add(subTask2Entity);

            var taskEntity = new TaskEntity();
            taskEntity.setPriority(TaskPriority.HIGH);
            taskEntity.setCompleted(false);
            taskEntity.setDescription("Task description");
            taskEntity.setId(id);
            taskEntity.setSubTasks(subTaskEntityList);

            var subTask1Dto = new SubTaskDto();
            subTask1Dto.setCompleted(true);
            subTask1Dto.setDescription("Sub Task 1 description");
            subTask1Dto.setId(1);

            var subTask2Dto = new SubTaskDto();
            subTask2Dto.setCompleted(false);
            subTask2Dto.setDescription("Sub Task 2 description");
            subTask2Dto.setId(2);

            var taskDto = new UpdateTaskRequestDto();
            taskDto.setDescription("Task description");
            taskDto.setCompleted(false);
            taskDto.setPriority(TaskPriority.HIGH);
            taskDto.setSubTasks(List.of(subTask1Dto, subTask2Dto));

            // when
            when(taskRepository.findById(any())).thenReturn(Optional.of(taskEntity));

            var fetchedTaskDto = taskService.getTaskById(id);

            // then
            assertNotNull(fetchedTaskDto);
            assertAll(
                    () -> assertEquals(taskDto.getDescription(), fetchedTaskDto.getDescription()),
                    () -> assertEquals(taskDto.isCompleted(), fetchedTaskDto.isCompleted()),
                    () -> assertEquals(taskDto.getPriority(), fetchedTaskDto.getPriority())
            );
            verify(taskRepository).findById(id);
        }

        @Test
        @DisplayName("Test for Get a non existing Task")
        void getTaskByIdForNonExistingTask() {
            // given
            int id = 1;
            var subTask1Entity = new SubTaskEntity();
            subTask1Entity.setCompleted(true);
            subTask1Entity.setDescription("Sub Task 1 description");
            subTask1Entity.setId(1);

            var subTask2Entity = new SubTaskEntity();
            subTask2Entity.setCompleted(false);
            subTask2Entity.setDescription("Sub Task 2 description");
            subTask2Entity.setId(2);

            List<SubTaskEntity> subTaskEntityList = new ArrayList<>();
            subTaskEntityList.add(subTask1Entity);
            subTaskEntityList.add(subTask2Entity);

            var taskEntity = new TaskEntity();
            taskEntity.setPriority(TaskPriority.HIGH);
            taskEntity.setCompleted(false);
            taskEntity.setDescription("Task description");
            taskEntity.setId(id);
            taskEntity.setSubTasks(subTaskEntityList);

            // when
            when(taskRepository.findById(any())).thenThrow(ResourceNotFoundException.class);

            Executable executable = () -> taskService.getTaskById(id);

            // then
            assertThrows(ResourceNotFoundException.class, executable);
            verify(taskRepository).findById(id);
        }
    }

    @Nested
    @DisplayName("Tests for delete task")
    class TestDeleteTask {
        @Test
        @DisplayName("Test for Delete task by id")
        void deleteTaskById() {
            // given
            int id = 1;
            var subTask1Entity = new SubTaskEntity();
            subTask1Entity.setCompleted(true);
            subTask1Entity.setDescription("Sub Task 1 description");
            subTask1Entity.setId(1);

            var subTask2Entity = new SubTaskEntity();
            subTask2Entity.setCompleted(false);
            subTask2Entity.setDescription("Sub Task 2 description");
            subTask2Entity.setId(2);

            List<SubTaskEntity> subTaskEntityList = new ArrayList<>();
            subTaskEntityList.add(subTask1Entity);
            subTaskEntityList.add(subTask2Entity);

            var taskEntity = new TaskEntity();
            taskEntity.setPriority(TaskPriority.HIGH);
            taskEntity.setCompleted(false);
            taskEntity.setDescription("Task description");
            taskEntity.setId(id);
            taskEntity.setSubTasks(subTaskEntityList);

            // when
            when(taskRepository.findById(any())).thenReturn(Optional.of(taskEntity));

            Executable executable = () -> taskService.deleteTaskById(id);

            // then
            assertDoesNotThrow(executable);
            verify(taskRepository).findById(id);
        }

        @Test
        @DisplayName("Test for Delete a non existing Task")
        void deleteTaskByIdForNonExistingTask() {
            // given
            int id = 1;
            var subTask1Entity = new SubTaskEntity();
            subTask1Entity.setCompleted(true);
            subTask1Entity.setDescription("Sub Task 1 description");
            subTask1Entity.setId(1);

            var subTask2Entity = new SubTaskEntity();
            subTask2Entity.setCompleted(false);
            subTask2Entity.setDescription("Sub Task 2 description");
            subTask2Entity.setId(2);

            List<SubTaskEntity> subTaskEntityList = new ArrayList<>();
            subTaskEntityList.add(subTask1Entity);
            subTaskEntityList.add(subTask2Entity);

            var taskEntity = new TaskEntity();
            taskEntity.setPriority(TaskPriority.HIGH);
            taskEntity.setCompleted(false);
            taskEntity.setDescription("Task description");
            taskEntity.setId(id);
            taskEntity.setSubTasks(subTaskEntityList);

            // when
            when(taskRepository.findById(any())).thenThrow(ResourceNotFoundException.class);

            Executable executable = () -> taskService.deleteTaskById(id);

            // then
            assertThrows(ResourceNotFoundException.class, executable);
            verify(taskRepository).findById(id);
        }
    }

    @Nested
    @DisplayName("Tests for get multiple tasks")
    class TestGetMultipleTasks {
        @Test
        @DisplayName("Test for Get tasks")
        void getTasks() {
            // given
            var taskEntity1 = new TaskEntity();
            taskEntity1.setPriority(TaskPriority.HIGH);
            taskEntity1.setCompleted(false);
            taskEntity1.setDescription("Task description 1");
            taskEntity1.setId(1);

            var taskEntity2 = new TaskEntity();
            taskEntity2.setPriority(TaskPriority.MEDIUM);
            taskEntity2.setCompleted(true);
            taskEntity2.setDescription("Task description 2");
            taskEntity2.setId(2);

            var taskEntity3 = new TaskEntity();
            taskEntity3.setPriority(TaskPriority.LOW);
            taskEntity3.setCompleted(false);
            taskEntity3.setDescription("Task description 3");
            taskEntity3.setId(3);

            List<TaskEntity> taskEntityList = new ArrayList<>();
            taskEntityList.add(taskEntity1);
            taskEntityList.add(taskEntity2);
            taskEntityList.add(taskEntity3);

            List<TaskResponseDto> taskDtoList = taskEntityList.stream().map(taskMapper::toDto).collect(Collectors.toList());

            // when
            when(taskRepository.findAll(any(Sort.class))).thenReturn(taskEntityList);

            var fetchedTaskDtos = taskService.getTasks(null, null, null, null);

            // then
            assertNotNull(fetchedTaskDtos);
            assertEquals(taskDtoList.size(), fetchedTaskDtos.size());
            for (int i = 0; i < taskDtoList.size(); i++) {
                var taskDto = taskDtoList.get(i);
                var fetchedTaskDto = fetchedTaskDtos.get(i);
                assertAll(
                        () -> assertEquals(taskDto.getId(), fetchedTaskDto.getId()),
                        () -> assertEquals(taskDto.getDescription(), fetchedTaskDto.getDescription()),
                        () -> assertEquals(taskDto.isCompleted(), fetchedTaskDto.isCompleted()),
                        () -> assertEquals(taskDto.getPriority(), fetchedTaskDto.getPriority())
                );
            }
            verify(taskRepository).findAll(Sort.unsorted());
        }

        @Test
        @DisplayName("Test for Get tasks and sort by priority in desc order")
        void getTasksAndSortByPriorityInDescOrder() {
            // given
            var taskEntity1 = new TaskEntity();
            taskEntity1.setPriority(TaskPriority.HIGH);
            taskEntity1.setCompleted(false);
            taskEntity1.setDescription("Task description 1");
            taskEntity1.setId(1);

            var taskEntity2 = new TaskEntity();
            taskEntity2.setPriority(TaskPriority.MEDIUM);
            taskEntity2.setCompleted(true);
            taskEntity2.setDescription("Task description 2");
            taskEntity2.setId(2);

            var taskEntity3 = new TaskEntity();
            taskEntity3.setPriority(TaskPriority.LOW);
            taskEntity3.setCompleted(false);
            taskEntity3.setDescription("Task description 3");
            taskEntity3.setId(3);

            var taskEntity4 = new TaskEntity();
            taskEntity4.setPriority(TaskPriority.HIGH);
            taskEntity4.setCompleted(false);
            taskEntity4.setDescription("Task description 4");
            taskEntity4.setId(4);

            List<TaskEntity> taskEntityList = new ArrayList<>();
            taskEntityList.add(taskEntity1);
            taskEntityList.add(taskEntity2);
            taskEntityList.add(taskEntity3);
            taskEntityList.add(taskEntity4);

            taskEntityList.sort(Comparator.comparing(
                    TaskEntity::getPriority
            ));

            List<TaskResponseDto> taskDtoList = taskEntityList.stream().map(taskMapper::toDto).collect(Collectors.toList());

            // when
            when(taskRepository.findAll(any(Sort.class))).thenReturn(taskEntityList);

            var fetchedTaskDtos = taskService.getTasks(null, null, SortColumn.PRIORITY, Sort.Direction.DESC);

            // then
            fetchedTaskDtos.forEach(System.out::println);
            assertNotNull(fetchedTaskDtos);
            assertEquals(taskDtoList.size(), fetchedTaskDtos.size());
            for (int i = 0; i < taskDtoList.size(); i++) {
                var taskDto = taskDtoList.get(i);
                var fetchedTaskDto = fetchedTaskDtos.get(i);
                assertAll(
                        () -> assertEquals(taskDto.getId(), fetchedTaskDto.getId()),
                        () -> assertEquals(taskDto.getDescription(), fetchedTaskDto.getDescription()),
                        () -> assertEquals(taskDto.isCompleted(), fetchedTaskDto.isCompleted()),
                        () -> assertEquals(taskDto.getPriority(), fetchedTaskDto.getPriority())
                );
            }
            verify(taskRepository).findAll(Sort.by(Sort.Direction.DESC, SortColumn.PRIORITY.getColumnName()));
        }

        @Test
        @DisplayName("Test for Get tasks of high priority")
        void getTasksFilteredByPriority() {
            // given
            var taskEntity1 = new TaskEntity();
            taskEntity1.setPriority(TaskPriority.HIGH);
            taskEntity1.setCompleted(false);
            taskEntity1.setDescription("Task description 1");
            taskEntity1.setId(1);

            var taskEntity2 = new TaskEntity();
            taskEntity2.setPriority(TaskPriority.HIGH);
            taskEntity2.setCompleted(true);
            taskEntity2.setDescription("Task description 2");
            taskEntity2.setId(2);

            List<TaskEntity> taskEntityList = new ArrayList<>();
            taskEntityList.add(taskEntity1);
            taskEntityList.add(taskEntity2);

            List<TaskResponseDto> taskDtoList = taskEntityList.stream().map(taskMapper::toDto).collect(Collectors.toList());

            // when
            when(taskRepository.findByPriority(any(), any(Sort.class))).thenReturn(taskEntityList);

            var fetchedTaskDtos = taskService.getTasks(TaskPriority.HIGH, null, null, null);

            // then
            assertNotNull(fetchedTaskDtos);
            assertEquals(taskDtoList.size(), fetchedTaskDtos.size());
            for (int i = 0; i < taskDtoList.size(); i++) {
                var taskDto = taskDtoList.get(i);
                var fetchedTaskDto = fetchedTaskDtos.get(i);
                assertAll(
                        () -> assertEquals(taskDto.getId(), fetchedTaskDto.getId()),
                        () -> assertEquals(taskDto.getDescription(), fetchedTaskDto.getDescription()),
                        () -> assertEquals(taskDto.isCompleted(), fetchedTaskDto.isCompleted()),
                        () -> assertEquals(taskDto.getPriority(), fetchedTaskDto.getPriority())
                );
            }
            verify(taskRepository).findByPriority(TaskPriority.HIGH, Sort.unsorted());
        }

        @Test
        @DisplayName("Test for Get tasks which are completed")
        void getTasksFilteredByCompleted() {
            // given
            var taskEntity1 = new TaskEntity();
            taskEntity1.setPriority(TaskPriority.MEDIUM);
            taskEntity1.setCompleted(true);
            taskEntity1.setDescription("Task description 1");
            taskEntity1.setId(1);

            var taskEntity2 = new TaskEntity();
            taskEntity2.setPriority(TaskPriority.HIGH);
            taskEntity2.setCompleted(true);
            taskEntity2.setDescription("Task description 2");
            taskEntity2.setId(2);

            List<TaskEntity> taskEntityList = new ArrayList<>();
            taskEntityList.add(taskEntity1);
            taskEntityList.add(taskEntity2);

            List<TaskResponseDto> taskDtoList = taskEntityList.stream().map(taskMapper::toDto).collect(Collectors.toList());

            // when
            when(taskRepository.findByCompleted(anyBoolean(), any(Sort.class))).thenReturn(taskEntityList);

            var fetchedTaskDtos = taskService.getTasks(null, true, null, null);

            // then
            assertNotNull(fetchedTaskDtos);
            assertEquals(taskDtoList.size(), fetchedTaskDtos.size());
            for (int i = 0; i < taskDtoList.size(); i++) {
                var taskDto = taskDtoList.get(i);
                var fetchedTaskDto = fetchedTaskDtos.get(i);
                assertAll(
                        () -> assertEquals(taskDto.getId(), fetchedTaskDto.getId()),
                        () -> assertEquals(taskDto.getDescription(), fetchedTaskDto.getDescription()),
                        () -> assertEquals(taskDto.isCompleted(), fetchedTaskDto.isCompleted()),
                        () -> assertEquals(taskDto.getPriority(), fetchedTaskDto.getPriority())
                );
            }
            verify(taskRepository).findByCompleted(true, Sort.unsorted());
        }

        @Test
        @DisplayName("Test for Get tasks of high priority and is completed")
        void getTasksFilteredByPriorityAndCompleted() {
            // given
            var taskEntity1 = new TaskEntity();
            taskEntity1.setPriority(TaskPriority.HIGH);
            taskEntity1.setCompleted(true);
            taskEntity1.setDescription("Task description 1");
            taskEntity1.setId(1);

            var taskEntity2 = new TaskEntity();
            taskEntity2.setPriority(TaskPriority.HIGH);
            taskEntity2.setCompleted(true);
            taskEntity2.setDescription("Task description 2");
            taskEntity2.setId(2);

            List<TaskEntity> taskEntityList = new ArrayList<>();
            taskEntityList.add(taskEntity1);
            taskEntityList.add(taskEntity2);

            List<TaskResponseDto> taskDtoList = taskEntityList.stream().map(taskMapper::toDto).collect(Collectors.toList());

            // when
            when(taskRepository.findByPriorityAndCompleted(any(), anyBoolean(), any(Sort.class))).thenReturn(taskEntityList);

            var fetchedTaskDtos = taskService.getTasks(TaskPriority.HIGH, true, null, null);

            // then
            assertNotNull(fetchedTaskDtos);
            assertEquals(taskDtoList.size(), fetchedTaskDtos.size());
            for (int i = 0; i < taskDtoList.size(); i++) {
                var taskDto = taskDtoList.get(i);
                var fetchedTaskDto = fetchedTaskDtos.get(i);
                assertAll(
                        () -> assertEquals(taskDto.getId(), fetchedTaskDto.getId()),
                        () -> assertEquals(taskDto.getDescription(), fetchedTaskDto.getDescription()),
                        () -> assertEquals(taskDto.isCompleted(), fetchedTaskDto.isCompleted()),
                        () -> assertEquals(taskDto.getPriority(), fetchedTaskDto.getPriority())
                );
            }
            verify(taskRepository).findByPriorityAndCompleted(TaskPriority.HIGH, true, Sort.unsorted());
        }
    }
}