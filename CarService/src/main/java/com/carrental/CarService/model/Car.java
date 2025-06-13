package com.carrental.CarService.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String brand;
    private String model;
    private boolean available;
    private double pricePerDay;

    @Column(name = "image_url", length = 1000)
    private String imageUrl; // Stores the Cloudinary image URL
}
