package com.carrental.CarListnerService.repository;

import com.carrental.CarListnerService.model.CarEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarEventRepository extends JpaRepository<CarEventEntity, Long> {
}
