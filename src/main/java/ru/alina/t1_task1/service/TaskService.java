package ru.alina.t1_task1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alina.t1_task1.dto.TaskDto;
import ru.alina.t1_task1.entity.Task;
import ru.alina.t1_task1.exception.TaskNotFoundException;
import ru.alina.t1_task1.mapper.TaskMapper;
import ru.alina.t1_task1.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskDto addTask(TaskDto taskDto) {
        Task task = TaskMapper.toTaskEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return TaskMapper.toTaskDto(savedTask);
    }

    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return TaskMapper.toTaskDto(task);
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    public TaskDto updateTaskById(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        boolean isUpdated = false;
        if (!task.getTitle().equals(taskDto.getTitle())) {
            task.setTitle(taskDto.getTitle());
            isUpdated = true;
        }
        if (!task.getDescription().equals(taskDto.getDescription())) {
            task.setDescription(taskDto.getDescription());
            isUpdated = true;
        }
        if (!task.getUserId().equals(taskDto.getUserId())) {
            task.setUserId(taskDto.getUserId());
            isUpdated = true;
        }
        return isUpdated ? TaskMapper.toTaskDto(taskRepository.save(task)) : TaskMapper.toTaskDto(task);
    }

    public void deleteTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.deleteById(id);
    }

}
