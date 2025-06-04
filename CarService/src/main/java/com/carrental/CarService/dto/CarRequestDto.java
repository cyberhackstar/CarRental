package com.carrental.CarService.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class CarRequestDto {

    @NotBlank(message = "Brand is required")
    @JsonProperty("brand")
    private String brand;

    @NotBlank(message = "Model is required")
    @JsonProperty("model")
    private String model;

    @NotNull(message = "Availability status is required")
    @JsonProperty("available")
    private Boolean available;

    @NotNull(message = "Price per day is required")
    @Positive(message = "Price must be positive")
    @JsonProperty("pricePerDay")
    private Double pricePerDay;

    // Constructors
    public CarRequestDto() {}

    public CarRequestDto(String brand, String model, Boolean available, Double pricePerDay) {
        this.brand = brand;
        this.model = model;
        this.available = available;
        this.pricePerDay = pricePerDay;
    }

    // Getters and Setters
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

public Boolean getAvailable() {
    return available;
}

public void setAvailable(Boolean available) {
    this.available = available;
}

    public Double getPricePerDay() {
        return pricePerDay;
    }

    public void setPricePerDay(Double pricePerDay) {
        this.pricePerDay = pricePerDay;
    }
}
