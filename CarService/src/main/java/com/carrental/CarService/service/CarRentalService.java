package com.carrental.CarService.service;

import com.carrental.CarService.dto.BookingEvent;
import com.carrental.CarService.dto.CarEvent;
import com.carrental.CarService.dto.CarRequestDto;
import com.carrental.CarService.messaging.BookingEventProducer;
import com.carrental.CarService.messaging.CarEventProducer;
import com.carrental.CarService.model.Booking;
import com.carrental.CarService.model.Car;
import com.carrental.CarService.repository.BookingRepository;
import com.carrental.CarService.repository.CarRepository;
import com.carrental.CarService.utility.CarMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarRentalService {

    private static final Logger logger = LoggerFactory.getLogger(CarRentalService.class);
    private static final String UPLOAD_DIR = "uploads";

    @Autowired
    private CarEventProducer carEventProducer;

    @Autowired
    private BookingEventProducer bookingEventProducer;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private BookingRepository bookingRepository;

    public List<Car> getAllAvailableCars() {
        logger.info("Fetching all available cars...");
        List<Car> cars = carRepository.findByAvailableTrue();
        logger.info("Found {} available cars", cars.size());
        return cars;
    }

    public Car createCar(CarRequestDto carDto, MultipartFile imageFile) throws IOException {
    logger.info("Creating new car: {} {}", carDto.getBrand(), carDto.getModel());
    Car car = CarMapper.toEntity(carDto);

    if (imageFile != null && !imageFile.isEmpty()) {
        // Save image to local file system
        String fileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(UPLOAD_DIR).resolve(fileName);
        Files.createDirectories(imagePath.getParent());
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        car.setImageUrl(fileName); // Save file name or path

        // Save image data to database
        car.setImageData(imageFile.getBytes());

        logger.info("Image uploaded and saved as: {}", fileName);
    }

    Car savedCar = carRepository.save(car);
    logger.info("Car saved with ID: {}", savedCar.getId());

    carEventProducer.sendCarEvent(CarEvent.builder()
            .carId(savedCar.getId())
            .brand(savedCar.getBrand())
            .model(savedCar.getModel())
            .available(savedCar.isAvailable())
            .pricePerDay(savedCar.getPricePerDay())
            .build());

    logger.info("Car event sent for car ID: {}", savedCar.getId());
    return savedCar;
}


    public Optional<Car> getCarById(Long id) {
        logger.info("Fetching car by ID: {}", id);
        return carRepository.findById(id);
    }

    public void deleteCar(Long id) {
        logger.info("Attempting to delete car with ID: {}", id);
        carRepository.findById(id).ifPresent(car -> {
            carRepository.deleteById(id);
            logger.info("Car deleted with ID: {}", id);
            if (car.getImageUrl() != null) {
                try {
                    Files.deleteIfExists(Paths.get(UPLOAD_DIR).resolve(car.getImageUrl()));
                    logger.info("Deleted image file: {}", car.getImageUrl());
                } catch (IOException e) {
                    logger.warn("Failed to delete image file: {}", car.getImageUrl(), e);
                }
            }
        });
    }

    public List<Booking> getAllBookings() {
        logger.info("Fetching all bookings...");
        List<Booking> bookings = bookingRepository.findAll();
        logger.info("Found {} bookings", bookings.size());
        return bookings;
    }

    public Booking bookCar(Booking booking) {
        logger.info("Booking request received for car ID: {}", booking.getCarId());

        if (booking.getCarId() == null) {
            logger.warn("Booking failed: carId is null");
            throw new IllegalArgumentException("Booking must include a valid carId.");
        }

        Car car = carRepository.findById(booking.getCarId())
                .filter(Car::isAvailable)
                .orElseThrow(() -> {
                    logger.warn("Car not available for booking: {}", booking.getCarId());
                    return new RuntimeException("Car not available for booking.");
                });

        car.setAvailable(false);
        carRepository.save(car);
        logger.info("Car marked as unavailable: {}", car.getId());

        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking saved with ID: {}", savedBooking.getId());

        bookingEventProducer.sendBookingEvent(BookingEvent.builder()
                .bookingId(savedBooking.getId())
                .carId(savedBooking.getCarId())
                .customerName(savedBooking.getCustomerName())
                .startDate(savedBooking.getStartDate())
                .endDate(savedBooking.getEndDate())
                .totalAmount(savedBooking.getTotalAmount())
                .build());

        logger.info("Booking event sent for booking ID: {}", savedBooking.getId());
        return savedBooking;
    }

    public Optional<Booking> getBooking(Long id) {
        logger.info("Fetching booking by ID: {}", id);
        return bookingRepository.findById(id);
    }
}
