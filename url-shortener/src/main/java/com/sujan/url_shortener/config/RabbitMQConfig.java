package com.sujan.url_shortener.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLICK_QUEUE="url.clicks.v2";
    public static final String CLICK_EXCHANGE="url.exchange";
    public static final String CLICK_ROUTING_KEY="url.click";

    public static final String DLQ_QUEUE = "url.clicks.dlq";
    public static final String DLQ_EXCHANGE = "url.dead-letter.exchange";
    public static final String DLQ_ROUTING_KEY = "url.clicks.dead";

    // Main queue
    @Bean
    public Queue clickQueue() {
        return QueueBuilder.durable(CLICK_QUEUE)
                .withArgument("x-dead-letter-exchange", DLQ_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", DLQ_ROUTING_KEY)
                .build();
    }

    // Main exchange
    @Bean
    public DirectExchange clickExchange() {
        return new DirectExchange(CLICK_EXCHANGE, true, false);
    }

    // Main queue binding
    @Bean
    public Binding clickBinding(
            @Qualifier("clickQueue") Queue clickQueue,
            @Qualifier("clickExchange") DirectExchange clickExchange) {

        return BindingBuilder.bind(clickQueue)
                .to(clickExchange)
                .with(CLICK_ROUTING_KEY);
    }

    // Dead Letter Queue
    @Bean
    public Queue deadLetterQueue() {
        return QueueBuilder.durable(DLQ_QUEUE).build();
    }

    // Dead Letter Exchange
    @Bean
    public DirectExchange deadLetterExchange() {
        return new DirectExchange(DLQ_EXCHANGE, true, false);
    }

    // Dead Letter Queue binding
    @Bean
    public Binding deadLetterBinding(
            @Qualifier("deadLetterQueue") Queue deadLetterQueue,
            @Qualifier("deadLetterExchange") DirectExchange deadLetterExchange) {

        return BindingBuilder.bind(deadLetterQueue)
                .to(deadLetterExchange)
                .with(DLQ_ROUTING_KEY);
    }
}
