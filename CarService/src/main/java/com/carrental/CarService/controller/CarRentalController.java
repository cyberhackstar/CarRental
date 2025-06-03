package com.carrental.CarService.controller;

import com.carrental.CarService.model.Booking;
import com.carrental.CarService.model.Car;
import com.carrental.CarService.service.CarRentalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CarRentalController {

    @Autowired
    private CarRentalService rentalService;

    @PostMapping("/cars")
    public Car createCar(@RequestBody Car car) {
    return rentalService.createCar(car);
    }


    @GetMapping("/cars")
    public List<Car> getAvailableCars() {
        return rentalService.getAllAvailableCars();
    }

    @GetMapping("/cars/{id}")
    public Optional<Car> getCarById(@PathVariable Long id) {
        return rentalService.getCarById(id);
    }

    @GetMapping("/bookings")
    public List<Booking> getBookings() {
        return rentalService.getAllBookings();
    }

    @PostMapping("/bookings")
    public Booking bookCar(@RequestBody Booking booking) {
        return rentalService.bookCar(booking);
    }

    @GetMapping("/bookings/{id}")
    public Optional<Booking> getBooking(@PathVariable Long id) {
        return rentalService.getBooking(id);
    }
}
