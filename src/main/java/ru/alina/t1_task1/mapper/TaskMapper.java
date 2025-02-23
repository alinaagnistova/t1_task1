package ru.alina.t1_task1.mapper;

import ru.alina.t1_task1.dto.TaskDto;
import ru.alina.t1_task1.entity.Task;

public class TaskMapper {
    public static TaskDto toTaskDto(Task task) {
        return TaskDto.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .userId(task.getUserId())
                .build();
    }
    public static Task toTaskEntity(TaskDto taskDto) {
        return Task.builder()
                .id(taskDto.getId())
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .userId(taskDto.getUserId())
                .build();
    }
}
