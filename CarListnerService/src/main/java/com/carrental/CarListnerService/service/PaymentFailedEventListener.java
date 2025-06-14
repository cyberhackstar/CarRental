package com.carrental.CarListnerService.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.carrental.common.dto.PaymentFailedEvent;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentFailedEventListener {

    private final EmailService emailService;

    public PaymentFailedEventListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "payment-failed-events")
    public void handlePaymentFailed(PaymentFailedEvent event) {
        log.warn("📥 Received Payment Failed Event: {}", event);

        try {
            emailService.sendPaymentFailureEmail(event);
            log.info("📧 Payment failure email sent.");
        } catch (Exception e) {
            log.error("❌ Failed to send payment failure email.", e);
        }
    }
}
