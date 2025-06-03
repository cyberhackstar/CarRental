package com.carrental.CarService.service;

import com.carrental.CarService.dto.BookingEvent;
import com.carrental.CarService.dto.CarEvent;
import com.carrental.CarService.messaging.BookingEventProducer;
import com.carrental.CarService.messaging.CarEventProducer;
import com.carrental.CarService.model.Booking;
import com.carrental.CarService.model.Car;
import com.carrental.CarService.repository.BookingRepository;
import com.carrental.CarService.repository.CarRepository;
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

   @Autowired
private CarEventProducer carEventProducer;

public Car createCar(Car car) {
    Car savedCar = carRepository.save(car);

    carEventProducer.sendCarEvent(CarEvent.builder()
            .carId(savedCar.getId())
            .brand(savedCar.getBrand())
            .model(savedCar.getModel())
            .available(savedCar.isAvailable())
            .pricePerDay(savedCar.getPricePerDay())
            .build());

    return savedCar;
}


    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Car> getCarById(Long id) {
        return carRepository.findById(id);
    }

    @Autowired
private BookingEventProducer bookingEventProducer;

public Booking bookCar(Booking booking) {
    if (booking.getCarId() == null) {
        throw new IllegalArgumentException("Booking must include a valid carId.");
    }

    Car car = carRepository.findById(booking.getCarId())
            .orElseThrow(() -> new RuntimeException("Car not found with ID: " + booking.getCarId()));

    if (!car.isAvailable()) {
        throw new RuntimeException("Car is not available for booking.");
    }

    car.setAvailable(false);
    carRepository.save(car);

    Booking savedBooking = bookingRepository.save(booking);

    bookingEventProducer.sendBookingEvent(BookingEvent.builder()
            .bookingId(savedBooking.getId())
            .carId(savedBooking.getCarId())
            .customerName(savedBooking.getCustomerName())
            .startDate(savedBooking.getStartDate())
            .endDate(savedBooking.getEndDate())
            .totalAmount(savedBooking.getTotalAmount())
            .build());

    return savedBooking;
}


    public Optional<Booking> getBooking(Long id) {
        return bookingRepository.findById(id);
    }
}
