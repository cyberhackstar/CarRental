package com.carrental.CarService.model;

@Entity
public class Booking {
    @Id @GeneratedValue
    private Long id;
    private Long carId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
    // Getters, Setters, Constructors
}
