package com.sujan.url_shortener.messaging;

import com.sujan.url_shortener.config.RabbitMQConfig;
import com.sujan.url_shortener.entity.Click;
import com.sujan.url_shortener.repository.ClickRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class ClickEventConsumer {

    private final ObjectMapper objectMapper;
    private final ClickRepository clickRepository;

    @RabbitListener(queues = RabbitMQConfig.CLICK_QUEUE)
    public void consumeClickEvent(String message){
        System.out.println("RabbitMQ message received: " + message);
       try {
           ClickEvent event=objectMapper.readValue(message, ClickEvent.class);
           Click click=new Click(
             null,
             event.shortCode(),
             event.clickedAt()
           );

           clickRepository.save(click);
           System.out.println("Click saved successfully");

       } catch (Exception e) {
           throw new IllegalArgumentException(
                   "Failed to process click event", e);
       }
    }
}
