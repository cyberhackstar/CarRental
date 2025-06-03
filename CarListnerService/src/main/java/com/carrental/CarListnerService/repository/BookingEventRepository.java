package com.carrental.CarListnerService.repository;

import com.carrental.CarListnerService.model.BookingEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingEventRepository extends JpaRepository<BookingEventEntity, Long> {
}
