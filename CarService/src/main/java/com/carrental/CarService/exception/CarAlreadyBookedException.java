package com.carrental.CarService.exception;

public class CarAlreadyBookedException extends RuntimeException {
    public CarAlreadyBookedException(String message) {
        super(message);
    }
}
