package com.swissre.app.it;

import com.swissre.app.dto.*;
import com.swissre.app.enums.ErrorDetail;
import com.swissre.app.enums.SortColumn;
import com.swissre.app.enums.TaskPriority;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("it-tests")
@DisplayName("Integration Tests")
public class TaskControllerIntegrationTest extends IntegrationTestUtils {

    @Nested
    @DisplayName("Tests for POST")
    class TestPostEndpoint {
        @Test
        @DisplayName("Test for save new task")
        void saveTaskIT() throws Exception {
            // when
            var savedTaskDto = createCommonTask();

            // then
            assertNotNull(savedTaskDto);
            var fetchedTaskFromDB = taskRepository.findById(savedTaskDto.getId());
            assertTrue(fetchedTaskFromDB.isPresent());
            assertAll(
                    () -> assertEquals(savedTaskDto.getId(), fetchedTaskFromDB.get().getId()),
                    () -> assertEquals(savedTaskDto.getDescription(), fetchedTaskFromDB.get().getDescription()),
                    () -> assertEquals(savedTaskDto.isCompleted(), fetchedTaskFromDB.get().isCompleted()),
                    () -> assertEquals(savedTaskDto.getPriority(), fetchedTaskFromDB.get().getPriority())
            );
        }
    }

    @Nested
    @DisplayName("Tests for PUT")
    class TestPutEndpoint {
        @Test
        @DisplayName("Test for update an existing task")
        void updateTaskIT() throws Exception {
            // given
            var subTask1Dto = new SubTaskDto();
            subTask1Dto.setCompleted(true);
            subTask1Dto.setDescription("Sub Task 1 description");

            var subTask2Dto = new SubTaskDto();
            subTask2Dto.setCompleted(false);
            subTask2Dto.setDescription("Sub Task 2 description");

            var updatedTaskDto = new UpdateTaskRequestDto();
            updatedTaskDto.setDescription("New Task description");
            updatedTaskDto.setCompleted(false);
            updatedTaskDto.setPriority(TaskPriority.HIGH);
            updatedTaskDto.setSubTasks(List.of(subTask1Dto, subTask2Dto));

            var request = objectMapper.writeValueAsString(updatedTaskDto);

            var savedTaskDto = createCommonTask();

            // when
            var response = mockMvc.perform(
                            put("/tasks/" + savedTaskDto.getId())
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(request)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var updatedTaskDtoResponse = objectMapper.readValue(response, TaskResponseDto.class);

            // then
            assertNotNull(updatedTaskDtoResponse);
            var fetchedTaskFromDB = taskRepository.findById(updatedTaskDtoResponse.getId());
            assertTrue(fetchedTaskFromDB.isPresent());
            assertAll(
                    () -> assertEquals(updatedTaskDtoResponse.getId(), fetchedTaskFromDB.get().getId()),
                    () -> assertEquals(updatedTaskDtoResponse.getDescription(), fetchedTaskFromDB.get().getDescription()),
                    () -> assertEquals(updatedTaskDtoResponse.isCompleted(), fetchedTaskFromDB.get().isCompleted()),
                    () -> assertEquals(updatedTaskDtoResponse.getPriority(), fetchedTaskFromDB.get().getPriority())
            );
        }

        @Test
        @DisplayName("Test for update a non existing task")
        void updateNonExistingTaskIT() throws Exception {
            // given
            var subTask1Dto = new SubTaskDto();
            subTask1Dto.setCompleted(true);
            subTask1Dto.setDescription("Sub Task 1 description");

            var subTask2Dto = new SubTaskDto();
            subTask2Dto.setCompleted(false);
            subTask2Dto.setDescription("Sub Task 2 description");

            var updatedTaskDto = new UpdateTaskRequestDto();
            updatedTaskDto.setDescription("New Task description");
            updatedTaskDto.setCompleted(false);
            updatedTaskDto.setPriority(TaskPriority.HIGH);
            updatedTaskDto.setSubTasks(List.of(subTask1Dto, subTask2Dto));

            var request = objectMapper.writeValueAsString(updatedTaskDto);

            // when
            var response = mockMvc.perform(
                            put("/tasks/" + 2)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(request)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
            assertNotNull(errorResponseDto);
            assertAll(
                    () -> assertEquals(ErrorDetail.TASK_NOT_FOUND.getCode(), errorResponseDto.getCode()),
                    () -> assertEquals(ErrorDetail.TASK_NOT_FOUND.getMessage(), errorResponseDto.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Tests for DELETE")
    class TestDeleteEndpoint {
        @Test
        @DisplayName("Test for delete an existing task")
        void deleteTaskIT() throws Exception {
            // when
            var savedTaskDto = createCommonTask();
            mockMvc.perform(
                            delete("/tasks/" + savedTaskDto.getId())
                    )
                    .andDo(print())
                    .andExpect(status().isNoContent())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
        }

        @Test
        @DisplayName("Test for delete a non existing task")
        void deleteNonExistingTaskIT() throws Exception {
            // when
            var response = mockMvc.perform(
                            delete("/tasks/" + 2)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
            assertNotNull(errorResponseDto);
            assertAll(
                    () -> assertEquals(ErrorDetail.TASK_NOT_FOUND.getCode(), errorResponseDto.getCode()),
                    () -> assertEquals(ErrorDetail.TASK_NOT_FOUND.getMessage(), errorResponseDto.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Tests for GET by id")
    class TestGetByIdEndpoint {
        @Test
        @DisplayName("Test for get an existing task")
        void getTaskIT() throws Exception {
            // when
            var savedTaskDto = createCommonTask();
            var response = mockMvc.perform(
                            get("/tasks/" + savedTaskDto.getId())
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var getTaskResponseDto = objectMapper.readValue(response, TaskResponseDto.class);
            // then
            assertNotNull(getTaskResponseDto);
            var fetchedTaskFromDB = taskRepository.findById(savedTaskDto.getId());
            assertTrue(fetchedTaskFromDB.isPresent());
            assertAll(
                    () -> assertEquals(getTaskResponseDto.getId(), fetchedTaskFromDB.get().getId()),
                    () -> assertEquals(getTaskResponseDto.getDescription(), fetchedTaskFromDB.get().getDescription()),
                    () -> assertEquals(getTaskResponseDto.isCompleted(), fetchedTaskFromDB.get().isCompleted()),
                    () -> assertEquals(getTaskResponseDto.getPriority(), fetchedTaskFromDB.get().getPriority())
            );
        }

        @Test
        @DisplayName("Test for get a non existing task")
        void getNonExistingTaskIT() throws Exception {
            // when
            var response = mockMvc.perform(
                            get("/tasks/" + 2)
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isNotFound())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var errorResponseDto = objectMapper.readValue(response, ErrorResponseDto.class);
            assertNotNull(errorResponseDto);
            assertAll(
                    () -> assertEquals(ErrorDetail.TASK_NOT_FOUND.getCode(), errorResponseDto.getCode()),
                    () -> assertEquals(ErrorDetail.TASK_NOT_FOUND.getMessage(), errorResponseDto.getMessage())
            );
        }
    }

    @Nested
    @DisplayName("Tests for GET")
    class TestGetEndpoint {
        @Test
        @DisplayName("Test for get multiple tasks")
        void getTasksIT() throws Exception {
            // when
            createMultipleCommonTasks();
            var response = mockMvc.perform(
                            get("/tasks")
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var getTaskResponseDto = objectMapper.readValue(response, List.class);
            // then
            assertNotNull(getTaskResponseDto);
            assertTrue(getTaskResponseDto.size() > 0);
        }

        @Test
        @DisplayName("Test for get multiple tasks sort by priority in desc order")
        void getTasksSortByPriorityInDescOrderIT() throws Exception {
            // when
            createMultipleCommonTasks();
            var response = mockMvc.perform(
                            get("/tasks")
                                    .queryParam("columnName", SortColumn.PRIORITY.name())
                                    .queryParam("sortDirection", Sort.Direction.DESC.name())
                                    .accept(MediaType.APPLICATION_JSON)
                    )
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andReturn()
                    .getResponse()
                    .getContentAsString();
            var getTaskResponseDto = objectMapper.readValue(response, List.class);
            // then
            assertNotNull(getTaskResponseDto);
            assertTrue(getTaskResponseDto.size() > 0);
        }
    }

    private TaskResponseDto createCommonTask() throws Exception {
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

        var request = objectMapper.writeValueAsString(taskDto);

        // when
        var response = mockMvc.perform(
                        post("/tasks")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(request)
                                .accept(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString();
        return objectMapper.readValue(response, TaskResponseDto.class);
    }

    private void createMultipleCommonTasks() {
        var taskDto1 = new CreateTaskRequestDto();
        taskDto1.setPriority(TaskPriority.HIGH);
        taskDto1.setCompleted(false);
        taskDto1.setDescription("Task description 1");

        var taskDto2 = new CreateTaskRequestDto();
        taskDto2.setPriority(TaskPriority.MEDIUM);
        taskDto2.setCompleted(true);
        taskDto2.setDescription("Task description 2");

        var taskDto3 = new CreateTaskRequestDto();
        taskDto3.setPriority(TaskPriority.LOW);
        taskDto3.setCompleted(false);
        taskDto3.setDescription("Task description 3");

        var taskDto4 = new CreateTaskRequestDto();
        taskDto4.setPriority(TaskPriority.HIGH);
        taskDto4.setCompleted(false);
        taskDto4.setDescription("Task description 4");

        List.of(taskDto1, taskDto2, taskDto3, taskDto4)
                .forEach(taskDto -> {
                    try {
                        var request = objectMapper.writeValueAsString(taskDto);

                        var response = mockMvc.perform(
                                        post("/tasks")
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .content(request)
                                                .accept(MediaType.APPLICATION_JSON)
                                )
                                .andDo(print())
                                .andExpect(status().isCreated())
                                .andReturn()
                                .getResponse()
                                .getContentAsString();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
    }
}
