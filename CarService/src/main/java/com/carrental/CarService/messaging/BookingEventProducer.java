package com.carrental.CarService.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.carrental.common.dto.BookingEvent;

// @Component
// public class BookingEventProducer {

//     private static final String QUEUE = "booking-events";

//     @Autowired
//     private JmsTemplate jmsTemplate;

//     public void sendBookingEvent(BookingEvent event) {
//         jmsTemplate.convertAndSend(QUEUE, event);
//     }
// }


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import com.carrental.common.dto.BookingEvent;

@Component
public class BookingEventProducer {

    private static final String QUEUE = "booking-events";

    private final RabbitTemplate rabbitTemplate;

    public BookingEventProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBookingEvent(BookingEvent event) {
        rabbitTemplate.convertAndSend(QUEUE, event);
    }
}
