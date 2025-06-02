package com.carrental.CarService.model;

@Entity
public class Car {
    @Id @GeneratedValue
    private Long id;
    private String model;
    private String brand;
    private boolean available;
    private double pricePerDay;
    // Getters, Setters, Constructors
}
