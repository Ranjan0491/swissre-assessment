package com.swissre.app.repositories;

import com.swissre.app.entities.TaskEntity;
import com.swissre.app.enums.TaskPriority;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<TaskEntity, Integer> {
    List<TaskEntity> findByPriorityAndCompleted(TaskPriority priority, boolean completed, Sort sort);
    List<TaskEntity> findByCompleted(boolean completed, Sort sort);
    List<TaskEntity> findByPriority(TaskPriority priority, Sort sort);
}
