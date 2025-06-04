package com.carrental.CarService.utility;

import com.carrental.CarService.dto.CarRequestDto;
import com.carrental.CarService.model.Car;

public class CarMapper {

    public static Car toEntity(CarRequestDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("CarRequestDto cannot be null");
        }

        Car car = new Car();
        car.setBrand(dto.getBrand());
        car.setModel(dto.getModel());
        car.setAvailable(dto.getAvailable());
        car.setPricePerDay(dto.getPricePerDay());

        // Optional: handle imageUrl if included in DTO
        

        return car;
    }
}
