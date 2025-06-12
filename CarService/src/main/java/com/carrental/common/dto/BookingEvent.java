package com.carrental.common.dto;

import lombok.*;
import java.io.Serializable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class BookingEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long bookingId;
    private Long carId;
    private String customerName;
    private String customerEmail;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
    private String carBrand;
    private String carModel;
    private double pricePerDay;
}
