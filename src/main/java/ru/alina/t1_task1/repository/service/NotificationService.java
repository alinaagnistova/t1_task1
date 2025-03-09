package ru.alina.t1_task1.repository.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.alina.t1_task1.repository.config.EmailProperties;
import ru.alina.t1_task1.repository.dto.TaskDto;
import ru.alina.t1_task1.repository.util.TaskEmailBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final JavaMailSender mailSender;

    private final TaskEmailBuilder taskEmailBuilder;

    private final EmailProperties emailProperties;

    public void notify(TaskDto taskDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        String recipient = emailProperties.getRecipient();
        message.setTo(recipient);
        message.setSubject(emailProperties.getSubject());
        message.setText(taskEmailBuilder.build(taskDto));
        mailSender.send(message);
        log.info("Notification sent: Task ID = {} to = {}", taskDto.getId(), recipient);
    }

}
