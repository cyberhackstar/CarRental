package com.carrental.common.dto;

import lombok.*;
import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentFailedEvent implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Long bookingId;
    private String customerName;
    private String customerEmail;
    private String reason; // Optional: add failure reason if available
}
