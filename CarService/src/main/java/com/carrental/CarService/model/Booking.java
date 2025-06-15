package com.carrental.CarService.model;

import jakarta.persistence.*;
import lombok.*;
// import com.carrental.CarService.model.PaymentStatus;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long carId;
    private String customerName;
    private String customerEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;

    private String orderId; // Razorpay order ID

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; // Enum to track payment status

    private String username; // ✅ New field to store logged-in user's username
}
