package com.flight.payment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "Payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String bookingId;
    private String stripeSessionId;
    private String stripePaymentIntentId;
    private String currency;
    private int amount;
    private int quantity;
    private String productName;

    private String status;
    private String receiptUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
