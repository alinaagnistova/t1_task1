package ru.alina.t1_task1.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.alina.t1_task1.dto.TaskDto;
@Slf4j
@Service
public class NotificationService {


    public void notify(TaskDto taskDto) {
        log.info("Notification sent: Task ID = {}, New Status = {}", taskDto.getId(), taskDto.getStatus());
    }
}
