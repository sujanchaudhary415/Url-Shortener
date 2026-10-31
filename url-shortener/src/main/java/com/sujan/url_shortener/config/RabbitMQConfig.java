package com.sujan.url_shortener.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String CLICK_QUEUE="url.clicks";
    public static final String CLICK_EXCHANGE="url.exchange";
    public static final String CLICK_ROUTING_KEY="url.click";

    @Bean
    public Queue clickQueue(){
       return new Queue(CLICK_QUEUE,true);
    }

    @Bean
    public DirectExchange clickExchange(){
        return new DirectExchange(CLICK_EXCHANGE,true,false);
    }

    @Bean
    public Binding clickBinding(Queue clickQueue,DirectExchange clickExchange){
       return BindingBuilder
               .bind(clickQueue)
               .to(clickExchange)
               .with(CLICK_ROUTING_KEY);
    }
}
