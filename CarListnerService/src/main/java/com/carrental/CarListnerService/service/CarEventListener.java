package com.carrental.CarListnerService.service;

import com.carrental.CarListnerService.dto.CarEvent;
import com.carrental.CarListnerService.model.CarEventEntity;
import com.carrental.CarListnerService.repository.CarEventRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CarEventListener {

    @Autowired
    private CarEventRepository repository;

    @JmsListener(destination = "car-events")
    public void handleCarEvent(CarEvent event) {
        log.info("📥 Received Car Event: {}", event);

        CarEventEntity entity = CarEventEntity.builder()
                .carId(event.getCarId())
                .brand(event.getBrand())
                .model(event.getModel())
                .available(event.isAvailable())
                .pricePerDay(event.getPricePerDay())
                .build();

        repository.save(entity);
        log.info("✅ Car event saved to database.");
    }
}
