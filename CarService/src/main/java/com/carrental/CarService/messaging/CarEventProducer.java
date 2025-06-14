package com.carrental.CarService.messaging;

import com.carrental.common.dto.CarEvent;
import org.springframework.stereotype.Component;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Component
public class CarEventProducer {

    private static final String QUEUE = "car-events";

    private final RabbitTemplate rabbitTemplate;

    public CarEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendCarEvent(CarEvent event) {
        rabbitTemplate.convertAndSend(QUEUE, event);
    }
}
