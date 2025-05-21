package com.flights.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Flight entity representing flight details")
public class Flights {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Schema(description = "Flight Number", example = "1001")
    @Column(unique = true, nullable = false)
    @Min(value = 1, message = "Flight number must be positive number")
    @Min(value = 1, message = "Flight number must be a positive number")
    private int flightNumber;

    @Schema(description = "Fare", example = "4500.00")
    @DecimalMin(value = "2000.0", inclusive = true, message = "Fare must be greater than 2000")
    private double fare;

    @Schema(description = "Source", example = "Bhopal")
    @NotBlank(message = "Source is required")
    private String source;

    @Schema(description = "Destination", example = "Pune")
    @NotBlank(message = "Destination is required")
    private String destination;

    @Schema(description = "Flight Date", example = "2025-05-15")
    @NotNull(message = "Flight Date is required")
    @FutureOrPresent(message = "Flight Date should be in present of future")
    private LocalDate flightDate;

    @Schema(description = "Total Seats", example = "50")
    private int totalSeats;

    @Schema(description = "Available Seats", example = "20")
    private int availableSeats;
}
