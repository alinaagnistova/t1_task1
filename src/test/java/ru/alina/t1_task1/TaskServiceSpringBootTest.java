package ru.alina.t1_task1;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.alina.t1_task1.config.TestConfig;
import ru.alina.t1_task1.dto.TaskDto;
import ru.alina.t1_task1.entity.Status;
import ru.alina.t1_task1.kafka.task.TaskProducer;
import ru.alina.t1_task1.repository.TaskRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
@Testcontainers
@Import(TestConfig.class)
public class TaskServiceSpringBootTest {

    @Autowired
    private TaskProducer taskProducer;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private EntityManager entityManager;

    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:15")
            .withDatabaseName("test_db")
            .withUsername("test")
            .withPassword("0000");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        postgreSQLContainer.start();
        registry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
    }

    @BeforeEach
    void setUp() {
        taskRepository.deleteAll();
    }

    @Test
    public void createTask_ShouldReturnCreatedTask() throws Exception {
        TaskDto taskDto = getTaskDto();

        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(taskDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value(taskDto.getTitle()))
                .andExpect(jsonPath("$.description").value(taskDto.getDescription()))
                .andExpect(jsonPath("$.status").value(taskDto.getStatus().name()))
                .andExpect(jsonPath("$.userId").value(taskDto.getUserId()))
                .andDo(print());
    }

    @Test
    @Transactional
    public void getTaskById_ShouldReturnTask_WhenTaskExists() throws Exception {
        TaskDto savedTask = getTaskDto();

        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedTask)))
                .andReturn().getResponse().getContentAsString();

        TaskDto createdTask = objectMapper.readValue(response, TaskDto.class);

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(get("/tasks/" + createdTask.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdTask.getId()))
                .andExpect(jsonPath("$.title").value(createdTask.getTitle()))
                .andExpect(jsonPath("$.description").value(createdTask.getDescription()))
                .andExpect(jsonPath("$.status").value(createdTask.getStatus().name()))
                .andExpect(jsonPath("$.userId").value(createdTask.getUserId()))
                .andDo(print());
    }

    @Test
    public void getTaskById_ShouldReturn404_WhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(get("/tasks/9999"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTask_ShouldReturnUpdatedTask() throws Exception {
        TaskDto savedTask = getTaskDto();

        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedTask)))
                .andReturn().getResponse().getContentAsString();

        TaskDto createdTask = objectMapper.readValue(response, TaskDto.class);

        entityManager.flush();
        entityManager.clear();

        TaskDto updatedTask = new TaskDto(createdTask.getId(), "Updated Task", "Test Description", Status.DONE, 1L);

        mockMvc.perform(put("/tasks/" + createdTask.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedTask)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(updatedTask.getTitle()))
                .andExpect(jsonPath("$.description").value(updatedTask.getDescription()))
                .andExpect(jsonPath("$.status").value(updatedTask.getStatus().name()))
                .andExpect(jsonPath("$.userId").value(updatedTask.getUserId()))
                .andDo(print());

        verify(taskProducer, times(1)).send(any(TaskDto.class));

    }

    @Test
    @Transactional
    public void deleteTask_ShouldReturnNoContent_WhenTaskExists() throws Exception {
        TaskDto savedTask = getTaskDto();

        String response = mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(savedTask)))
                .andReturn().getResponse().getContentAsString();

        TaskDto createdTask = objectMapper.readValue(response, TaskDto.class);

        entityManager.flush();
        entityManager.clear();

        mockMvc.perform(delete("/tasks/" + createdTask.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tasks/" + createdTask.getId()))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteTask_ShouldReturn404_WhenTaskDoesNotExist() throws Exception {
        mockMvc.perform(delete("/tasks/9999"))
                .andExpect(status().isNotFound());
    }

    private TaskDto getTaskDto() {
        return TaskDto.builder()
                .title("Test task")
                .description("Test Description")
                .status(Status.TO_DO)
                .userId(1L)
                .build();
    }
}