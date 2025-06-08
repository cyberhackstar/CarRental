package com.carrental.common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long bookingId;
    private Long carId;
    private String customerName;
    private String customerEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
}
