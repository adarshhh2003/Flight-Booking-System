package com.flight.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MessagingDetails {

    private String recipientEmail;
    private String passengerName;
    private String bookingId;
    private String flightId;
    private String source;
    private String destination;
    private String seatNumber;
    private double totalAmountPaid;
    private String status;

}
