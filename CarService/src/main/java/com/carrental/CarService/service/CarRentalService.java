package com.carrental.CarService.service;

import com.carrental.common.dto.CarEvent;
import com.carrental.CarService.dto.CarRequestDto;
import com.carrental.CarService.messaging.BookingEventProducer;
import com.carrental.CarService.messaging.CarEventProducer;
import com.carrental.CarService.model.Booking;
import com.carrental.CarService.model.Car;
import com.carrental.CarService.model.PaymentStatus;
import com.carrental.CarService.payment.PaymentService;
import com.carrental.CarService.repository.BookingRepository;
import com.carrental.CarService.repository.CarRepository;
import com.carrental.CarService.utility.CarMapper;
import com.carrental.common.dto.BookingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import org.json.JSONObject;



@Service
public class CarRentalService {

    private static final Logger logger = LoggerFactory.getLogger(CarRentalService.class);
    @Value("${car.image.upload-dir}")
    private String uploadDir;

    @Autowired
    private CarEventProducer carEventProducer;

    @Autowired
    private BookingEventProducer bookingEventProducer;

    @Autowired
    private CarRepository carRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private Cloudinary cloudinary;

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
            logger.info("Uploading optimized image to Cloudinary...");

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadOptions = (Map<String, Object>) ObjectUtils.asMap(
                    "transformation", new Transformation<>().width(1000)
                            .crop("scale")
                            .quality("auto")
                            .fetchFormat("auto"));

            @SuppressWarnings("unchecked")
            Map<String, Object> uploadResult = (Map<String, Object>) cloudinary.uploader().upload(imageFile.getBytes(), uploadOptions);
            String imageUrl = uploadResult.get("secure_url").toString();
            car.setImageUrl(imageUrl);

            logger.info("Optimized image uploaded successfully: {}", imageUrl);
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

            if (car.getImageUrl() != null && car.getImageUrl().contains("cloudinary.com")) {
                try {
                    String publicId = extractPublicIdFromUrl(car.getImageUrl());
                    cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    logger.info("Deleted image from Cloudinary: {}", publicId);
                } catch (Exception e) {
                    logger.warn("Failed to delete image from Cloudinary: {}", car.getImageUrl(), e);
                }
            }
        });
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        // Example: https://res.cloudinary.com/demo/image/upload/v1234567890/sample.jpg
        // Extract "sample" from the URL
        String[] parts = imageUrl.split("/");
        String fileName = parts[parts.length - 1];
        return fileName.substring(0, fileName.lastIndexOf('.'));
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
                .orElseThrow(() -> new RuntimeException("Car not found."));

        // Check for overlapping bookings
        List<Booking> overlapping = bookingRepository.findOverlappingBookings(
                booking.getCarId(),
                booking.getStartDate(),
                booking.getEndDate());

        if (!overlapping.isEmpty()) {
            logger.warn("Car is already booked for the selected dates: {}", booking.getCarId());
            throw new IllegalStateException("Car is already booked for the selected dates.");
        }

        // Calculate total amount
        long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate()) + 1;
        booking.setTotalAmount(days * car.getPricePerDay());

        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking saved with ID: {}", savedBooking.getId());

        bookingEventProducer.sendBookingEvent(BookingEvent.builder()
                .bookingId(savedBooking.getId())
                .carId(savedBooking.getCarId())
                .customerName(savedBooking.getCustomerName())
                .customerEmail(savedBooking.getCustomerEmail())
                .startDate(savedBooking.getStartDate())
                .endDate(savedBooking.getEndDate())
                .totalAmount(savedBooking.getTotalAmount())
                .carBrand(car.getBrand())
                .carModel(car.getModel())
                .pricePerDay(car.getPricePerDay())
                .build());

        logger.info("Booking event sent for booking ID: {}", savedBooking.getId());
        return savedBooking;
    }

    public List<Car> getAvailableCarsBetween(LocalDate startDate, LocalDate endDate) {
        logger.info("Fetching available cars between {} and {}", startDate, endDate);

        try {
            List<Long> bookedCarIds = bookingRepository.findBookedCarIdsBetween(startDate, endDate);
            List<Car> availableCars;
            if (bookedCarIds.isEmpty()) {
                availableCars = carRepository.findAll(); // All cars are available
            } else {
                availableCars = carRepository.findByIdNotIn(bookedCarIds);
            }
            logger.info("Found {} available cars", availableCars.size());
            return availableCars;
        } catch (Exception e) {
            logger.error("Error in getAvailableCarsBetween: ", e);
            throw e;
        }
    }

    public Optional<Booking> getBooking(Long id) {
        logger.info("Fetching booking by ID: {}", id);
        return bookingRepository.findById(id);
    }

    public Map<String, Object> bookCarWithPayment(Booking booking) {
    logger.info("Booking request received for car ID: {}", booking.getCarId());

    if (booking.getCarId() == null) {
        logger.warn("Booking failed: carId is null");
        throw new IllegalArgumentException("Booking must include a valid carId.");
    }

    Car car = carRepository.findById(booking.getCarId())
            .orElseThrow(() -> new RuntimeException("Car not found."));

    List<Booking> overlapping = bookingRepository.findOverlappingBookings(
            booking.getCarId(),
            booking.getStartDate(),
            booking.getEndDate());

    if (!overlapping.isEmpty()) {
        logger.warn("Car is already booked for the selected dates: {}", booking.getCarId());
        throw new IllegalStateException("Car is already booked for the selected dates.");
    }

    long days = ChronoUnit.DAYS.between(booking.getStartDate(), booking.getEndDate()) + 1;
    booking.setTotalAmount(days * car.getPricePerDay());

    String razorpayOrderJson;
    try {
        razorpayOrderJson = paymentService.createOrder(booking.getTotalAmount());
        JSONObject razorpayOrder = new JSONObject(razorpayOrderJson);
        booking.setOrderId(razorpayOrder.getString("id")); // Save Razorpay order ID
        booking.setPaymentStatus(PaymentStatus.PENDING);   // Mark as pending
    } catch (com.razorpay.RazorpayException e) {
        logger.error("Failed to create Razorpay order", e);
        throw new RuntimeException("Payment order creation failed", e);
    }

    Booking savedBooking = bookingRepository.save(booking);
    logger.info("Booking saved with ID: {} and status: PENDING", savedBooking.getId());

    Map<String, Object> response = new HashMap<>();
    response.put("booking", savedBooking);
    response.put("razorpayOrder", razorpayOrderJson);
    return response;
}

}
