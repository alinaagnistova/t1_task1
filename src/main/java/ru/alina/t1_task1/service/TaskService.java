package ru.alina.t1_task1.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alina.t1_task1.aspect.annotation.CustomExceptionHandling;
import ru.alina.t1_task1.aspect.annotation.CustomLogging;
import ru.alina.t1_task1.dto.TaskDto;
import ru.alina.t1_task1.entity.Task;
import ru.alina.t1_task1.exception.TaskNotFoundException;
import ru.alina.t1_task1.kafka.task.TaskProducer;
import ru.alina.t1_task1.mapper.TaskMapper;
import ru.alina.t1_task1.repository.TaskRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {
    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;
    private final TaskProducer taskProducer;

    @CustomLogging
    public TaskDto addTask(TaskDto taskDto) {
        Task task = taskMapper.toTaskEntity(taskDto);
        Task savedTask = taskRepository.save(task);
        return taskMapper.toTaskDto(savedTask);
    }

    @CustomExceptionHandling
    public TaskDto getTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        return taskMapper.toTaskDto(task);
    }

    public List<TaskDto> getAllTasks() {
        return taskRepository.findAll().stream()
                .map(taskMapper::toTaskDto)
                .collect(Collectors.toList());
    }

    @CustomExceptionHandling
    public TaskDto updateTaskById(Long id, TaskDto taskDto) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        boolean isUpdated = false;
        boolean isStatusChanged = false;
        if (!task.getTitle().equals(taskDto.getTitle())) {
            task.setTitle(taskDto.getTitle());
            isUpdated = true;
        }
        if (!task.getDescription().equals(taskDto.getDescription())) {
            task.setDescription(taskDto.getDescription());
            isUpdated = true;
        }
        if (!task.getStatus().equals(taskDto.getStatus())) {
            task.setStatus(taskDto.getStatus());
            isStatusChanged = true;
            isUpdated = true;
        }
        if (!task.getUserId().equals(taskDto.getUserId())) {
            task.setUserId(taskDto.getUserId());
            isUpdated = true;
        }
        TaskDto newTaskDto = isUpdated ? taskMapper.toTaskDto(taskRepository.save(task)) : taskMapper.toTaskDto(task);
        if (isStatusChanged){
            taskProducer.send(newTaskDto);
        }
        return newTaskDto;
    }

    @CustomExceptionHandling
    public void deleteTaskById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(() -> new TaskNotFoundException(id));
        taskRepository.deleteById(id);
    }

}
