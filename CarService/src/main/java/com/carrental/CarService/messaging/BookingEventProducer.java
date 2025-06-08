package com.carrental.CarService.messaging;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.carrental.common.dto.BookingEvent;

@Component
public class BookingEventProducer {

    private static final String QUEUE = "booking-events";

    @Autowired
    private JmsTemplate jmsTemplate;

    public void sendBookingEvent(BookingEvent event) {
        jmsTemplate.convertAndSend(QUEUE, event);
    }
}
