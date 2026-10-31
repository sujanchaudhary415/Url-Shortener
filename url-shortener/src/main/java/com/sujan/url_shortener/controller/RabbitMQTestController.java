package com.sujan.url_shortener.controller;

import com.sujan.url_shortener.messaging.ClickEventProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RabbitMQTestController {

    private final ClickEventProducer producer;

    public RabbitMQTestController(ClickEventProducer producer) {
        this.producer = producer;
    }

    @PostMapping("/api/test-rabbitmq")
    public String testRabbitMQ(
            @RequestParam(defaultValue = "yjRBGOr") String shortCode) {

        producer.publishClick(shortCode);
        return "Click event published for " + shortCode;
    }
}
