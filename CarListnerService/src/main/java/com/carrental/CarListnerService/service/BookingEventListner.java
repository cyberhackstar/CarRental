package com.carrental.CarListnerService.service;

import com.car.listener.dto.BookingEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingEventListener {

    @JmsListener(destination = "booking-events")
    public void handleBookingEvent(BookingEvent event) {
        log.info("🎯 Received Booking Event: {}", event);

        // Example: store in DB, send email, update analytics
        // For now, we just log
    }
}
