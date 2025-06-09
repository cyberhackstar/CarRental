package com.carrental.CarService.controller;

import com.carrental.CarService.dto.CarRequestDto;
import com.carrental.CarService.dto.CarResponseDto;
import com.carrental.CarService.model.Booking;
import com.carrental.CarService.model.Car;
import com.carrental.CarService.service.CarRentalService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CarRentalController {

    private static final Logger logger = LoggerFactory.getLogger(CarRentalController.class);
    // private static final String UPLOAD_DIR = "uploads";
    @Value("${car.image.upload-dir}")
    private String uploadDir;

    @Autowired
    private CarRentalService carRentalService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping(value = "/cars", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponseDto> createCar(
            @RequestPart("car") String carJson,
            @RequestPart("image") MultipartFile imageFile) throws IOException {

        logger.info("Received request to create a new car with image: {}", imageFile.getOriginalFilename());
        CarRequestDto carDto = objectMapper.readValue(carJson, CarRequestDto.class);
        Car savedCar = carRentalService.createCar(carDto, imageFile);
        logger.info("Car created successfully with ID: {}", savedCar.getId());
        return ResponseEntity.ok(new CarResponseDto(savedCar));
    }

    @GetMapping("/cars")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CarResponseDto>> getAvailableCars() {
        logger.info("Fetching all available cars...");

        try {
            List<Car> cars = carRentalService.getAllAvailableCars();
            List<CarResponseDto> response = cars.stream()
                    .map(CarResponseDto::new)
                    .toList();
            logger.info("Successfully fetched {} available cars", response.size());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Failed to fetch available cars", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(List.of());
        }

    }

    @GetMapping("/available")
    public ResponseEntity<List<Car>> getAvailableCars(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        return ResponseEntity.ok(carRentalService.getAvailableCarsBetween(startDate, endDate));
    }

    @GetMapping("/cars/{id}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarResponseDto> getCarById(@PathVariable Long id) {
        logger.info("Fetching car with ID: {}", id);
        return carRentalService.getCarById(id)
                .map(car -> {
                    logger.info("Car found with ID: {}", id);
                    return ResponseEntity.ok(new CarResponseDto(car));
                })
                .orElseGet(() -> {
                    logger.warn("Car not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/cars/image/{filename:.+}")
    // @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Resource> getCarImage(@PathVariable String filename) throws IOException {
        logger.info("Fetching image with filename: {}", filename);
        Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
        logger.info("Looking for image at: {}", filePath.toAbsolutePath());

        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists()) {
            logger.warn("Image not found: {}", filename);
            return ResponseEntity.notFound().build();
        }

        String contentType = Files.probeContentType(filePath);
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(contentType));
        headers.set("Access-Control-Allow-Origin", "*");
        headers.set("Cross-Origin-Resource-Policy", "cross-origin");

        logger.info("Image served successfully: {}", filename);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }

    @GetMapping("/bookings")
    public ResponseEntity<List<Booking>> getBookings() {
        logger.info("Fetching all bookings...");
        List<Booking> bookings = carRentalService.getAllBookings();
        logger.info("Fetched {} bookings", bookings.size());
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/bookings")
    public ResponseEntity<Booking> bookCar(@RequestBody Booking booking) {
        logger.info("Booking request received for car ID: {}", booking.getCarId());
        Booking savedBooking = carRentalService.bookCar(booking);
        logger.info("Booking successful with ID: {}", savedBooking.getId());
        return ResponseEntity.ok(savedBooking);
    }

    @GetMapping("/bookings/{id}")
    public ResponseEntity<Booking> getBooking(@PathVariable Long id) {
        logger.info("Fetching booking with ID: {}", id);
        return carRentalService.getBooking(id)
                .map(booking -> {
                    logger.info("Booking found with ID: {}", id);
                    return ResponseEntity.ok(booking);
                })
                .orElseGet(() -> {
                    logger.warn("Booking not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }
}
