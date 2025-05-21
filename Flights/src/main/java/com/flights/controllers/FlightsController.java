package com.flights.controllers;

import com.flights.entity.Flights;
import com.flights.services.FlightsServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/flights")
@Tag(name = "Add Flights", description = "Admin can add flights and it's details")
public class FlightsController {

    private FlightsServiceImpl flightsService;

    @Autowired
    public FlightsController(FlightsServiceImpl flightsService) {
        this.flightsService = flightsService;
    }

    @Operation(summary = "Add Flight", description = "Admin can add flights with flight details.")
    @PostMapping("/add")
    public ResponseEntity<Flights> addFlights(@Valid @RequestBody Flights flights) {
        Flights flight = flightsService.addFlights(flights);
        return ResponseEntity.ok(flight);
    }

    @Operation(summary = "Get All Flights", description = "User can get all the available flights")
    @GetMapping("/getAll")
    public ResponseEntity<List<Flights>> getAllFlights() {
        List<Flights> flights = flightsService.getAllFlights();
        return ResponseEntity.ok(flights);
    }

    @Operation(summary = "Get Flight Details", description = "User can get the flight details by flight number")
    @GetMapping("/getDetails/{flightNumber}")
    public ResponseEntity<Flights> getDetails(@PathVariable int flightNumber) {
        Flights flights = flightsService.getDetails(flightNumber);
        return ResponseEntity.ok(flights);
    }

    @Operation(summary = "Check Flight Availability", description = "User can get whether the seats available or not")
    @GetMapping("/check-availability/{flightNumber}")
    public ResponseEntity<Boolean> isSeatAvailable(@PathVariable int flightNumber) {
        boolean check = flightsService.isSeatAvailable(flightNumber);
        return ResponseEntity.ok(check);
    }

    @Operation(summary = "Get Flights Sorted By Price", description = "User can get the flight sorted by price")
    @GetMapping("/sortedByPrice")
    public ResponseEntity<List<Flights>> getFlightsSortedByPrice() {
        return ResponseEntity.ok(flightsService.getFlightsSortedByPrice());
    }

    @Operation(summary = "Update Flight Details", description = "Admin can update the flight details by flight number")
    @PutMapping("/update/{flightNumber}")
    public ResponseEntity<Flights> updateFlights(@Valid @PathVariable int flightNumber, @RequestBody Flights updateFlights) {
        Flights flight = flightsService.updateFlights(flightNumber, updateFlights);
        return ResponseEntity.ok(flight);
    }

    @Operation(summary = "Update Available Seat", description = "Booking Service can update the available seat info according to bookings")
    @PutMapping("/update/availableSeat")
    public ResponseEntity<Flights> updateAvailableSeats(@RequestParam int flightNumber, @RequestParam int availableSeats) {
        Flights flights = flightsService.updateAvailableSeats(flightNumber, availableSeats);
        return ResponseEntity.ok(flights);
    }

    @Operation(summary = "Delete Flight", description = "Admin can delete the flight by flight number")
    @DeleteMapping("/delete/{flightNumber}")
    public ResponseEntity<String> deleteFlights(@Valid @PathVariable int flightNumber) {
        flightsService.deleteFlights(flightNumber);
        return ResponseEntity.ok("Flight deleted successfully");
    }

}
