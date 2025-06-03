package com.carrental.CarListnerService.service;

import com.carrental.CarListnerService.dto.BookingEvent;
import com.carrental.CarListnerService.model.BookingEventEntity;
import com.carrental.CarListnerService.repository.BookingEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingEventListener {

    @Autowired
    private BookingEventRepository repository;

    @JmsListener(destination = "booking-events")
    public void handleBookingEvent(BookingEvent event) {
        log.info("📥 Received Booking Event: {}", event);

        // Save to DB
        BookingEventEntity entity = BookingEventEntity.builder()
                .bookingId(event.getBookingId())
                .carId(event.getCarId())
                .customerName(event.getCustomerName())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .totalAmount(event.getTotalAmount())
                .build();

        repository.save(entity);
        log.info("✅ Booking event saved to database.");
    }
}
