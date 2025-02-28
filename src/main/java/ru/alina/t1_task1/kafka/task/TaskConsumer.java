package ru.alina.t1_task1.kafka.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import ru.alina.t1_task1.dto.TaskDto;
import ru.alina.t1_task1.service.NotificationService;

@Slf4j
@Component
@RequiredArgsConstructor
public class TaskConsumer {

    private final NotificationService notificationService;
    @KafkaListener(topics = "${kafka.topic.task-status}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(TaskDto taskDto) {
        log.info("Received Task Update: ID = {}, Status = {}", taskDto.getId(), taskDto.getStatus());
        notificationService.notify(taskDto);
    }

}
