package ru.alina.t1_task1.kafka.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import ru.alina.t1_task1.dto.TaskDto;
@Slf4j
@Component
@RequiredArgsConstructor
public class TaskProducer {
    @Value("${kafka.topic.task-status}")
    private String clientTopic;
    private final KafkaTemplate<String, TaskDto> kafkaTemplate;

    public void send(TaskDto taskDto) {
        log.info("Sending task {}", taskDto);
        kafkaTemplate.send(clientTopic, taskDto.getId().toString(), taskDto);
    }
}
