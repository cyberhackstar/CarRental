package com.carrental.CarListnerService.model;

package com.car.listener.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "booking_events")
public class BookingEventEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long bookingId;
    private Long carId;
    private String customerName;
    private LocalDate startDate;
    private LocalDate endDate;
    private double totalAmount;
}
