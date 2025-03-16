package ru.alina.t1_task1.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.alina.t1_task1.kafka.task.TaskProducer;

@Configuration
public class TestConfig {

    @Bean
    public TaskProducer taskProducer() {
        return Mockito.mock(TaskProducer.class);
    }
}
