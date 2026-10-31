package com.sujan.url_shortener.messaging;

import com.sujan.url_shortener.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class ClickEventProducer {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    public void publishClick(String shortCode){
        try {
            ClickEvent event=new ClickEvent(
              shortCode,
              Instant.now()
            );

            String message= objectMapper.writeValueAsString(event);

            rabbitTemplate.convertAndSend(RabbitMQConfig.CLICK_EXCHANGE,RabbitMQConfig.CLICK_ROUTING_KEY,message);

        }
        catch(Exception e){
            throw new IllegalStateException("Failed to publish click event",e);
        }
    }

}
