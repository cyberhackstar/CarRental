package com.carrental.CarService.dto;

import lombok.*;

import java.time.LocalDate;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long bookingId;
    private Long carId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
}
