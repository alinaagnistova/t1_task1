package ru.alina.t1_task1;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alina.t1_task1.dto.TaskDto;
import ru.alina.t1_task1.entity.Status;
import ru.alina.t1_task1.entity.Task;
import ru.alina.t1_task1.exception.TaskNotFoundException;
import ru.alina.t1_task1.mapper.TaskMapper;
import ru.alina.t1_task1.repository.TaskRepository;
import ru.alina.t1_task1.service.TaskService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @Mock
    private TaskMapper taskMapper;

    @InjectMocks
    private TaskService taskService;


    @Test
    public void addTask_ShouldSaveAndReturnTask() {

        Task savedTask = getTask();
        TaskDto taskDto = getTaskDto();

        when(taskMapper.toTaskEntity(any(TaskDto.class))).thenReturn(savedTask);
        when(taskMapper.toTaskDto(any(Task.class))).thenReturn(taskDto);
        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);


        TaskDto result = taskService.addTask(taskDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(savedTask.getId(), result.getId());
        Assertions.assertEquals(savedTask.getTitle(), result.getTitle());
        Assertions.assertEquals(savedTask.getDescription(), result.getDescription());
        Assertions.assertEquals(savedTask.getStatus(), result.getStatus());
        Assertions.assertEquals(savedTask.getUserId(), result.getUserId());

        verify(taskMapper).toTaskEntity(any(TaskDto.class));
        verify(taskMapper).toTaskDto(any(Task.class));
        verify(taskRepository).save(any(Task.class));

    }

    @Test
    public void getTaskById_ShouldReturnTask_WhenTaskExists() {
        Long taskId = 1L;
        Task task = getTask();
        TaskDto taskDto = getTaskDto();

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));
        when(taskMapper.toTaskDto(any(Task.class))).thenReturn(taskDto);

        TaskDto result = taskService.getTaskById(taskId);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(taskId, result.getId());
        Assertions.assertEquals("test-title", result.getTitle());
        Assertions.assertEquals("test-description", result.getDescription());


        verify(taskRepository).findById(taskId);
        verify(taskMapper).toTaskDto(task);
    }

    @Test
    public void getTaskById_ShouldThrowException_WhenTaskDoesNotExist() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(taskId));

        verify(taskRepository).findById(taskId);
        verify(taskMapper, never()).toTaskDto(any(Task.class));

    }

    @Test
    public void updateTaskById_ShouldUpdateAndReturnTask_WhenTaskExists() {
        Long taskId = 1L;
        Task oldTask = getTask();
        Task updatedTask = new Task(taskId, "new-title", "new-description", Status.TO_DO, 1L);
        TaskDto updatedTaskDto = new TaskDto(taskId, updatedTask.getTitle(), updatedTask.getDescription(), updatedTask.getStatus(), updatedTask.getUserId());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);
        when(taskMapper.toTaskDto(any(Task.class))).thenReturn(updatedTaskDto);

        TaskDto result = taskService.updateTaskById(taskId, updatedTaskDto);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(taskId, result.getId());
        Assertions.assertEquals(updatedTaskDto.getTitle(), result.getTitle());
        Assertions.assertEquals(updatedTaskDto.getDescription(), result.getDescription());
        Assertions.assertEquals(updatedTaskDto.getStatus(), result.getStatus());
        Assertions.assertEquals(updatedTaskDto.getUserId(), result.getUserId());

        verify(taskRepository).findById(taskId);
        verify(taskRepository).save(any(Task.class));
        verify(taskMapper).toTaskDto(any(Task.class));
    }

    @Test
    public void updateTaskById_ShouldThrowException_WhenTaskDoesNotExist() {
        Long taskId = 1L;
        TaskDto taskDto = getTaskDto();
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.updateTaskById(taskId, taskDto));
        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper, never()).toTaskDto(any(Task.class));
    }

    @Test
    public void updateTaskById_ShouldNotUpdate_WhenNoChanges(){
        Long taskId = 1L;
        Task oldTask = getTask();
        TaskDto sameTaskDto = new TaskDto(null, oldTask.getTitle(), oldTask.getDescription(), oldTask.getStatus(), oldTask.getUserId());

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(oldTask));

        when(taskMapper.toTaskDto(any(Task.class))).thenAnswer(invocation -> {
            Task taskArg = invocation.getArgument(0);
            return new TaskDto(taskArg.getId(), taskArg.getTitle(), taskArg.getDescription(), taskArg.getStatus(), taskArg.getUserId());
        });

        TaskDto result = taskService.updateTaskById(taskId, sameTaskDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(taskId, result.getId());


        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).save(any(Task.class));
        verify(taskMapper).toTaskDto(any(Task.class));
    }

    @Test
    public void deleteTaskById_ShouldDeleteTask_WhenTaskExists() {
        Long taskId = 1L;
        Task task = getTask();
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        taskService.deleteTaskById(taskId);

        verify(taskRepository).findById(taskId);
        verify(taskRepository).deleteById(taskId);
    }

    @Test
    public void deleteTaskById_ShouldThrowException_WhenTaskDoesNotExist() {
        Long taskId = 1L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());
        Assertions.assertThrows(TaskNotFoundException.class, () -> taskService.deleteTaskById(taskId));

        verify(taskRepository).findById(taskId);
        verify(taskRepository, never()).deleteById(anyLong());
    }


    private Task getTask(){
        return Task.builder()
                .id(1L)
                .title("test-title")
                .description("test-description")
                .status(Status.TO_DO)
                .userId(1L)
                .build();
    }

    private TaskDto getTaskDto(){
        return TaskDto.builder()
                .id(1L)
                .title("test-title")
                .description("test-description")
                .status(Status.TO_DO)
                .userId(1L)
                .build();
    }

}
