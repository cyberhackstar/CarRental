package com.carrental.CarService.dto;

import com.carrental.CarService.model.Car;

public class CarResponseDto {
    private Long id;
    private String brand;
    private String model;
    private boolean available;
    private double pricePerDay;
    private String imageUrl;

    public CarResponseDto(Car car) {
        this.id = car.getId();
        this.brand = car.getBrand();
        this.model = car.getModel();
        this.available = car.isAvailable();
        this.pricePerDay = car.getPricePerDay();
        this.imageUrl = car.getImageUrl(); // Direct Cloudinary URL
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
