package ru.alina.t1_task1.dto;

import lombok.*;

@Builder
@Data
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Long userId;
}
