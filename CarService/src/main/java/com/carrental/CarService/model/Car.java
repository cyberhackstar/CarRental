package com.carrental.CarService.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Column(name = "image_url")
    private String imageUrl; // Path or URL to the image

    // @Lob
    // @Basic(fetch = FetchType.LAZY)
    // @Column(name = "image_data", columnDefinition = "bytea")
    // @JsonIgnore // Optional: prevent large binary data from being serialized in API responses
    // private byte[] imageData;
}
