package ru.alina.t1_task1.repository.exception;

public class TaskNotFoundException extends RuntimeException {
    public TaskNotFoundException(Long id) {
        super("Task with ID " + id + " not found");
    }
}
