package ru.alina.t1_task1.repository.mapper;

import org.springframework.stereotype.Component;
import ru.alina.t1_task1.repository.dto.TaskDto;
import ru.alina.t1_task1.repository.entity.Task;

@Component
public class TaskMapper {

    public TaskDto toTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .userId(task.getUserId())
                .build();
    }

    public Task toTaskEntity(TaskDto taskDto) {
        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .status(taskDto.getStatus())
                .userId(taskDto.getUserId())
                .build();
    }
}
