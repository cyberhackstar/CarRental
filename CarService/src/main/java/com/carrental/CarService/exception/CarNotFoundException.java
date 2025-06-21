package com.carrental.CarService.exception;

public class CarNotFoundException extends RuntimeException {
    
    public CarNotFoundException(String message) {
        super(message);
    }
}
