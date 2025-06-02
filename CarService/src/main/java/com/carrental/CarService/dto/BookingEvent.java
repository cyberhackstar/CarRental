package com.carrental.CarService.dto;


import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEvent {
    private Long bookingId;
    private Long carId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
}
