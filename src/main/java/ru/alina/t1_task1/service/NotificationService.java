package ru.alina.t1_task1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import ru.alina.t1_task1.dto.TaskDto;
import ru.alina.t1_task1.util.TaskEmailBuilder;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

        private final JavaMailSender mailSender;
        private final TaskEmailBuilder taskEmailBuilder;
        @Value("${email.task-change.subject}")
        private String emailSubject;
        @Value("${email.task-change.recipient}")
        private String recipientEmail;

    public void notify(TaskDto taskDto) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(emailSubject);
        message.setText(taskEmailBuilder.build(taskDto));
        mailSender.send(message);
        log.info("Notification sent: Task ID = {} to = {}", taskDto.getId(), recipientEmail);
    }

}
