package com.search.entity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
public class Flights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Min(value = 1, message = "Flight number must be a positive number")
    private int flightNumber;

    @DecimalMin(value = "2000.0", inclusive = true, message = "Fare must be greater than 2000")
    private double fare;

    @NotBlank(message = "Source is required")
    private String source;

    @NotBlank(message = "Destination is required")
    private String destination;

    @NotNull(message = "Flight Date is required")
    @FutureOrPresent(message = "Flight Date should be in present of future")
    private LocalDate flightDate;
}
