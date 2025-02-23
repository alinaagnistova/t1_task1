package ru.alina.t1_task1.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class TaskDto {
    private Long id;
    private String title;
    private String description;
    private Long userId;
}
