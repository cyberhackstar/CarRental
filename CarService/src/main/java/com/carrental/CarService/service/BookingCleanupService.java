package com.carrental.CarService.service;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.carrental.CarService.model.Booking;
import com.carrental.CarService.model.PaymentStatus;
import com.carrental.CarService.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingCleanupService {

    private final BookingRepository bookingRepository;

    @Scheduled(fixedRate = 86400000) // Runs every 24 hour
    public void deleteFailedBookings() {
        List<Booking> failedBookings = bookingRepository.findByPaymentStatus(PaymentStatus.FAILED);
        bookingRepository.deleteAll(failedBookings);
        System.out.println("Deleted failed bookings: " + failedBookings.size());
    }
}
