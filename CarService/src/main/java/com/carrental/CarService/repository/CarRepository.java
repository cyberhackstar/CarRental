package com.carrental.CarService.repository;

import com.carrental.CarService.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByAvailableTrue();
    List<Car> findByIdNotIn(List<Long> ids);

}
