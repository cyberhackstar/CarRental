package com.carrental.CarService.messaging;

import com.carrental.common.dto.CarEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class CarEventProducer {

    private static final String QUEUE = "car-events";

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendCarEvent(CarEvent event) {
        jmsTemplate.convertAndSend(QUEUE, event);
    }
}
