// package com.carrental.CarListnerService.service;

// // import the BookingEvent DTO from the correct package
// import com.carrental.common.dto.BookingEvent;

// import com.carrental.CarListnerService.model.BookingEventEntity;
// import com.carrental.CarListnerService.repository.BookingEventRepository;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.jms.annotation.JmsListener;
// import org.springframework.stereotype.Component;

// @Slf4j
// @Component
// public class BookingEventListener {

//     @Autowired
//     private BookingEventRepository repository;

//     @Autowired
//     private EmailService emailService;

//     @JmsListener(destination = "booking-events")
//     public void handleBookingEvent(BookingEvent event) {
//         log.info("📥 Received Booking Event: {}", event);

//         // Save to DB
//         BookingEventEntity entity = BookingEventEntity.builder()
//                 .bookingId(event.getBookingId())
//                 .carId(event.getCarId())
//                 .customerName(event.getCustomerName())
//                 .customerEmail(event.getCustomerEmail())
//                 .startDate(event.getStartDate())
//                 .endDate(event.getEndDate())
//                 .totalAmount(event.getTotalAmount())
//                 .build();

//         repository.save(entity);
//         log.info("✅ Booking event saved to database.");

//         // Send email
//         try {
//             emailService.sendBookingConfirmation(event);
//             log.info("📧 Booking confirmation email sent.");
//         } catch (Exception e) {
//             log.error("❌ Failed to send booking confirmation email.", e);
//         }
//     }

// }

package com.carrental.CarListnerService.service;

import com.carrental.common.dto.BookingEvent;
import com.carrental.CarListnerService.model.BookingEventEntity;
import com.carrental.CarListnerService.repository.BookingEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BookingEventListener {

    @Autowired
    private BookingEventRepository repository;

    @Autowired
    private EmailService emailService;

    @RabbitListener(queues = "booking-events")
    public void handleBookingEvent(BookingEvent event) {
        log.info("📥 Received Booking Event: {}", event);

        BookingEventEntity entity = BookingEventEntity.builder()
                .bookingId(event.getBookingId())
                .carId(event.getCarId())
                .customerName(event.getCustomerName())
                .customerEmail(event.getCustomerEmail())
                .startDate(event.getStartDate())
                .endDate(event.getEndDate())
                .totalAmount(event.getTotalAmount())
                .build();

        repository.save(entity);
        log.info("✅ Booking event saved to database.");

        try {
            emailService.sendBookingConfirmation(event);
            log.info("📧 Booking confirmation email sent.");
        } catch (Exception e) {
            log.error("❌ Failed to send booking confirmation email.", e);
        }
    }
}

