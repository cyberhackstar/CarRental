package com.carrental.CarService.RabitMQ;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public Queue carEventsQueue() {
        return new Queue("car-events", true);
    }

    @Bean
    public Queue bookingEventsQueue() {
        return new Queue("booking-events", true);
    }
}
