package com.flight.booking.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Bookings")
@Schema(description = "Booking entity representing booking details")
public class Bookings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Schema(description = "Email Id", example = "adarshpatel@gmail.com")
    private String emailId;

    @Schema(description = "Flight Id", example = "1001")
    private int flightId;

    @Schema(description = "Passenger Name", example = "Adarsh Patel")
    private String passengerName;

    @Schema(description = "Booking Date", example = "2025-05-15")
    private LocalDate bookingDate;

    @Schema(description = "Status", example = "Confirmed/Failed")
    private String status;

    @Schema(description = "Seat Number", example = "S50")
    private String seatNumber;

    public Bookings(String emailId, int flightNumber, String passengerName, LocalDate bookingDate, String status, Object o) {
        this.emailId = emailId;
        this.flightId = flightNumber;
        this.passengerName = passengerName;
        this.bookingDate = bookingDate;
        this.status = status;
        this.seatNumber = (String) o;
    }


//    public Bookings(String emailId, int flightNumber, String passengerName, LocalDateTime bookingDate, String status, String seatNumber) {
//        this.emailId = emailId;
//        this.flightId = flightNumber;
//        this.passengerName = passengerName;
//        this.bookingDate = LocalDate.from(bookingDate);
//        this.status = status;
//        this.seatNumber = seatNumber;
//    }
}
