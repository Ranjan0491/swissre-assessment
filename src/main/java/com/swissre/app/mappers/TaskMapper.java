package com.swissre.app.mappers;

import com.swissre.app.dto.CreateTaskRequestDto;
import com.swissre.app.dto.TaskResponseDto;
import com.swissre.app.dto.UpdateTaskRequestDto;
import com.swissre.app.entities.TaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TaskMapper {
    TaskResponseDto toDto(TaskEntity taskEntity);
    TaskEntity toEntity(CreateTaskRequestDto taskDto);
    TaskEntity toEntity(UpdateTaskRequestDto taskDto);

    @Mapping(target = "id", ignore = true)
    void merge(@MappingTarget TaskEntity oldTaskEntity, TaskEntity newTaskEntity);
}
