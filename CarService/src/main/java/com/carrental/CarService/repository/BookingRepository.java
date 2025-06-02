package com.carrental.CarService.repository;

import com.car.service.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailableTrue();
}
