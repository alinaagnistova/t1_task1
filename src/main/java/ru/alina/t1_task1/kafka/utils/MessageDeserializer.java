package ru.alina.t1_task1.kafka.utils;



import java.nio.charset.StandardCharsets;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.header.Headers;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Slf4j
public class MessageDeserializer<T> extends JsonDeserializer<T> {


    private static String getMessage(byte[] data){
        return new String(data, StandardCharsets.UTF_8);
    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try{
            return super.deserialize(topic, data);
        }catch(Exception e){
            log.warn("Error during deserialize message {}", new String(data, StandardCharsets.UTF_8),e);
            return null;
        }
    }
    @Override
    public T deserialize(String topic, Headers headers, byte[] data) {
        try{
            return super.deserialize(topic, headers, data);
        }catch(Exception e){
            log.warn("Error during deserialize message {}", new String(data, StandardCharsets.UTF_8),e);
            return null;
        }
    }

}
