package com.carrental.CarService.repository;

import com.carrental.CarService.model.Booking;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query("SELECT b FROM Booking b WHERE b.carId = :carId AND " +
            "(:startDate <= b.endDate AND :endDate >= b.startDate)")
    List<Booking> findOverlappingBookings(@Param("carId") Long carId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT DISTINCT b.carId FROM Booking b WHERE " +
            "(:startDate <= b.endDate AND :endDate >= b.startDate)")
    List<Long> findBookedCarIdsBetween(@Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

}
