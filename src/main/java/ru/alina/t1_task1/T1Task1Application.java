package ru.alina.t1_task1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import ru.alina.t1_task1.config.EmailProperties;


@SpringBootApplication
@EnableAspectJAutoProxy
@EnableConfigurationProperties(EmailProperties.class)
public class T1Task1Application {
    public static void main(String[] args) {
        SpringApplication.run(T1Task1Application.class, args);
    }

}
