package com.carrental.CarService.messaging;

import org.springframework.stereotype.Component;

import com.carrental.common.dto.PaymentFailedEvent;

import lombok.RequiredArgsConstructor;

import org.springframework.amqp.rabbit.core.RabbitTemplate;

@Component
@RequiredArgsConstructor
public class PaymentFailedEventProducer {

    private final RabbitTemplate rabbitTemplate;
    private static final String QUEUE = "payment-failed-events";

    public void sendPaymentFailedEvent(PaymentFailedEvent event) {
        rabbitTemplate.convertAndSend(QUEUE, event);
    }
}
