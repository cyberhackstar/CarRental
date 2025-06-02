package com.carrental.CarService.service;

import com.car.service.model.Booking;
import com.car.service.model.Car;
import com.car.service.repository.BookingRepository;
import com.car.service.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarRentalService {
    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<Car> getAllAvailableCars() {
        return carRepository.findByAvailableTrue();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    public Booking bookCar(Booking booking) {
        Car car = carRepository.findById(booking.getCarId()).orElseThrow();
        if (!car.isAvailable()) throw new RuntimeException("Car not available");

        car.setAvailable(false);
        carRepository.save(car);

        return bookingRepository.save(booking);
    }

    public Optional<Booking> getBooking(Long id) {
        return bookingRepository.findById(id);
    }
}
