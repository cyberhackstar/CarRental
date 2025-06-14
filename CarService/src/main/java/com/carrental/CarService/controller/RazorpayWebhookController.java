package com.carrental.CarService.controller;

import com.carrental.CarService.model.Booking;
import com.carrental.CarService.model.PaymentStatus;
import com.carrental.CarService.repository.BookingRepository;
import com.carrental.CarService.repository.CarRepository;
import com.carrental.CarService.payment.PaymentService;
import com.carrental.CarService.messaging.BookingEventProducer;
import com.carrental.CarService.messaging.PaymentFailedEventProducer;
import com.carrental.common.dto.BookingEvent;
import com.carrental.common.dto.PaymentFailedEvent;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.BufferedReader;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class RazorpayWebhookController {

    private final PaymentService paymentService;
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;
    private final BookingEventProducer bookingEventProducer;
    private final PaymentFailedEventProducer paymentFailedEventProducer;

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(HttpServletRequest request,
                                                @RequestHeader("X-Razorpay-Signature") String signature) {
        try {
            // Read raw body from request
            String payload = new BufferedReader(request.getReader())
                    .lines()
                    .collect(Collectors.joining(System.lineSeparator()));

            // Log for debugging
            System.out.println("Received webhook payload: " + payload);
            System.out.println("Received signature: " + signature);

            // Verify Razorpay signature
            if (!paymentService.verifyWebhookSignature(payload, signature)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
            }

            // Parse JSON payload
            JSONObject event = new JSONObject(payload);
            String eventType = event.getString("event");

            JSONObject paymentEntity = event.getJSONObject("payload")
                                            .getJSONObject("payment")
                                            .getJSONObject("entity");

            String orderId = paymentEntity.getString("order_id");

            Optional<Booking> bookingOpt = bookingRepository.findByOrderId(orderId);
            if (bookingOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Booking not found for order ID");
            }

            Booking booking = bookingOpt.get();

            if ("payment.captured".equals(eventType)) {
                booking.setPaymentStatus(PaymentStatus.SUCCESS);
                bookingRepository.save(booking);

                var car = carRepository.findById(booking.getCarId()).orElseThrow();

                BookingEvent bookingEvent = BookingEvent.builder()
                        .bookingId(booking.getId())
                        .carId(booking.getCarId())
                        .customerName(booking.getCustomerName())
                        .customerEmail(booking.getCustomerEmail())
                        .startDate(booking.getStartDate())
                        .endDate(booking.getEndDate())
                        .totalAmount(booking.getTotalAmount())
                        .carBrand(car.getBrand())
                        .carModel(car.getModel())
                        .pricePerDay(car.getPricePerDay())
                        .build();

                bookingEventProducer.sendBookingEvent(bookingEvent);
                return ResponseEntity.ok("Payment confirmed and booking event sent");

            } else if ("payment.failed".equals(eventType)) {
                booking.setPaymentStatus(PaymentStatus.FAILED);
                bookingRepository.save(booking);

                PaymentFailedEvent failedEvent = PaymentFailedEvent.builder()
                        .bookingId(booking.getId())
                        .customerName(booking.getCustomerName())
                        .customerEmail(booking.getCustomerEmail())
                        .reason("Payment failed via Razorpay")
                        .build();

                paymentFailedEventProducer.sendPaymentFailedEvent(failedEvent);
                return ResponseEntity.ok("Payment failed event sent");
            }

            return ResponseEntity.ok("Event ignored");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error processing webhook");
        }
    }
}
