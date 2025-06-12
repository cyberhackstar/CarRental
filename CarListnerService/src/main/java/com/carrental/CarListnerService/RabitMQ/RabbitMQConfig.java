package com.carrental.CarListnerService.RabitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMQConfig {

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public Queue carEventsQueue() {
        return new Queue("car-events", true);
    }

    @Bean
    public Queue bookingEventsQueue() {
        return new Queue("booking-events", true);
    }
}
