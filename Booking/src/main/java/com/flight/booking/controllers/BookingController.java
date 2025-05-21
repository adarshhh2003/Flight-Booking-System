package com.flight.booking.controllers;

import com.flight.booking.dto.BookingDTO;
import com.flight.booking.dto.StripeResponse;
import com.flight.booking.entity.Bookings;
import com.flight.booking.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Tag(name = "Flight Book", description = "Book for flight with some details")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Operation(summary = "Book Flight", description = "Book flight with some booking details")
    @PostMapping("/book")
    public ResponseEntity<?> bookFlight(@RequestBody BookingDTO bookingDTO) {
        try {
            StripeResponse paymentResponse = bookingService.bookFlight(bookingDTO);
            return ResponseEntity.ok(paymentResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @Operation(summary = "Cancel Book Flight", description = "Cancel any booked flight with the Booking Id")
    @DeleteMapping("/cancel/{bookingId}")
    public ResponseEntity<?> cancelBooking(@PathVariable int bookingId) {
        try {
            bookingService.cancelBooking(bookingId);
            return ResponseEntity.ok("Booking cancelled Successfully");
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get Booking Details", description = "Get booking details of the flights with Booking Id")
    @GetMapping("/getDetails/{id}")
    public ResponseEntity<Bookings> getBookingDetails(@PathVariable int id) {
        Bookings bookingDetails = bookingService.getBookingDetails(id);
        return ResponseEntity.ok(bookingDetails);
    }

    @Operation(summary = "Get Booking Details", description = "Get the booking details of the flight with email Id")
    @GetMapping("/getBookings/{email}")
    public ResponseEntity<List<Bookings>> getBookingsByUser(@PathVariable String email) {
        List<Bookings> bookings = bookingService.getBookingsByUser(email);

        if(bookings.isEmpty()) {
            return new ResponseEntity<>(bookings, HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(bookings);
    }

}
