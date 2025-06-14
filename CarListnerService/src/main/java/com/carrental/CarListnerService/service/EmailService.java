package com.carrental.CarListnerService.service;

import com.carrental.common.dto.BookingEvent;
import com.carrental.common.dto.PaymentFailedEvent;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void sendBookingConfirmation(BookingEvent event) throws MessagingException, IOException {
        String htmlTemplate = loadHtmlTemplate("templates/booking_confirmation_template.html");

        String htmlContent = htmlTemplate
                .replace("{{customer_name}}", event.getCustomerName())
                .replace("{{service_details}}", event.getBookingId().toString())
                .replace("{{car_brand}}", event.getCarBrand())
                .replace("{{car_model}}", event.getCarModel())
                .replace("{{price_per_day}}", String.valueOf(event.getPricePerDay()))
                .replace("{{start_date}}", event.getStartDate().toString())
                .replace("{{end_date}}", event.getEndDate().toString())
                .replace("{{total_amount}}", String.valueOf(event.getTotalAmount()));

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(event.getCustomerEmail());
        helper.setSubject("Your Car Rental Booking Confirmation");
        helper.setText(htmlContent, true); // true = HTML

        mailSender.send(message);
    }

    public void sendPaymentFailureEmail(PaymentFailedEvent event) throws MessagingException, IOException {
        String htmlTemplate = loadHtmlTemplate("templates/payment_failed_template.html");

        String htmlContent = htmlTemplate
                .replace("{{customer_name}}", event.getCustomerName())
                .replace("{{booking_id}}", String.valueOf(event.getBookingId()))
                .replace("{{reason}}", event.getReason());

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(event.getCustomerEmail());
        helper.setSubject("Payment Failed for Your Car Booking");
        helper.setText(htmlContent, true);

        mailSender.send(message);
    }

    private String loadHtmlTemplate(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        try (InputStream inputStream = resource.getInputStream()) {
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
