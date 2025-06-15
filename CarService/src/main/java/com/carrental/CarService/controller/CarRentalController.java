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
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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

    /**
     * Create a new car with image upload to Cloudinary
     */
    @PostMapping(value = "/cars", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CarResponseDto> createCar(
            @RequestPart("car") String carJson,
            @RequestPart("image") MultipartFile imageFile) throws IOException {

        logger.info("Received request to create a new car with image: {}", imageFile.getOriginalFilename());
        CarRequestDto carDto = objectMapper.readValue(carJson, CarRequestDto.class);
        Car savedCar = carRentalService.createCar(carDto, imageFile);
        logger.info("Car created successfully with ID: {}", savedCar.getId());
        return ResponseEntity.ok(new CarResponseDto(savedCar));
    }

    /**
     * Get all available cars with image URLs
     */
    @GetMapping("/cars")
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(List.of());
        }
    }

    @PutMapping(value = "/cars/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<CarResponseDto> updateCar(
            @PathVariable Long id,
            @RequestPart("car") String carJson,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) throws IOException {

        logger.info("Received request to update car with ID: {}", id);
        CarRequestDto carDto = objectMapper.readValue(carJson, CarRequestDto.class);
        Car updatedCar = carRentalService.updateCar(id, carDto, imageFile);
        logger.info("Car updated successfully with ID: {}", updatedCar.getId());
        return ResponseEntity.ok(new CarResponseDto(updatedCar));
    }

    // ✅ Get all cars
    @GetMapping("/cars/all")
    public ResponseEntity<List<Car>> getAllCars() {
        logger.info("Fetching all cars...");
        List<Car> cars = carRentalService.getAllCars();
        logger.info("Found {} cars", cars.size());
        return ResponseEntity.ok(cars);
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

    @DeleteMapping("/cars/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        logger.info("Received request to delete car with ID: {}", id);
        try {
            carRentalService.deleteCar(id);
            logger.info("Car deleted successfully with ID: {}", id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (Exception e) {
            logger.error("Failed to delete car with ID: {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
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

    @GetMapping("/bookings/by-username")
    public ResponseEntity<List<Booking>> getBookingsByUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        List<Booking> bookings = carRentalService.getBookingsByUsername(username);
        return ResponseEntity.ok(bookings);
    }

    @PostMapping("/bookings")
    public ResponseEntity<Booking> bookCar(@RequestBody Booking booking) {
        logger.info("Booking request received for car ID: {}", booking.getCarId());
        Booking savedBooking = carRentalService.bookCar(booking);
        logger.info("Booking successful with ID: {}", savedBooking.getId());
        return ResponseEntity.ok(savedBooking);
    }

    @PostMapping("/bookings/with-payment")
    public ResponseEntity<Map<String, Object>> bookCarWithPayment(@RequestBody Booking booking) {
        // ✅ Get the logged-in user's username
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        // ✅ Set the username in the booking object
        booking.setUsername(username);

        // ✅ Proceed with booking
        Map<String, Object> response = carRentalService.bookCarWithPayment(booking);
        return ResponseEntity.ok(response);
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
