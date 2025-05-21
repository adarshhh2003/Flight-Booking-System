package com.flight.booking.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@Data
@AllArgsConstructor
public class Flights {

    private int flightNumber;
    private double fare;
    private String source;
    private String destination;
    private LocalDate flightDate;
    private int totalSeats;
    private int availableSeats;

}
