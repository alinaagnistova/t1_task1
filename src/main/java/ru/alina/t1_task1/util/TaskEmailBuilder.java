package ru.alina.t1_task1.util;

import org.springframework.stereotype.Component;
import ru.alina.t1_task1.dto.TaskDto;

@Component
public class TaskEmailBuilder {

    public String build(TaskDto taskDto){
        return String.format(
                "New task notification:\n\n" +
                        "ID: %d\n" +
                        "Title: %s\n" +
                        "Description: %s\n" +
                        "Status: %s\n" +
                        "User ID: %d\n",
                taskDto.getId(),
                taskDto.getTitle(),
                taskDto.getDescription(),
                taskDto.getStatus(),
                taskDto.getUserId()
        );
    }
}
