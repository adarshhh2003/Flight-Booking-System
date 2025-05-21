package com.flight.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {

    private int amount;
    private int flightId;
    private String currency;

    private String bookingId;
    private String userID;
}
