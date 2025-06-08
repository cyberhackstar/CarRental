package com.carrental.common.dto;

import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long carId;
    private String brand;
    private String model;
    private boolean available;
    private double pricePerDay;
}
