package com.carrental.CarListnerService.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.carrental.common.dto.BookingEvent;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendBookingConfirmation(BookingEvent event) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(event.getCustomerEmail()); // Replace with event.getCustomerEmail() if available
        message.setSubject("Booking Confirmation");
        message.setText("Hi " + event.getCustomerName() + ",\n\nYour booking is confirmed from " +
                event.getStartDate() + " to " + event.getEndDate() +
                ".\nTotal Amount: ₹" + event.getTotalAmount() + "\n\nThank you!");

        mailSender.send(message);
    }
}
