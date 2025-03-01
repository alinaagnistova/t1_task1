package ru.alina.t1_task1.dto;

import lombok.*;
import ru.alina.t1_task1.entity.Status;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Status status;
    private Long userId;
}
