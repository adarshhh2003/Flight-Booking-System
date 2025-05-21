package com.flight.booking.clients;

import com.flight.booking.dto.Flights;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "FLIGHT-SERVICE")
public interface FlightServiceClient {

    @PutMapping("flights/update/availableSeat")
    public Flights updateAvailableSeats(@RequestParam int flightNumber, @RequestParam int availableSeats);

    @GetMapping("/flights/getDetails/{flightNumber}")
    public Flights getDetails(@PathVariable int flightNumber);

    @GetMapping("/flights/check-availability/{flightNumber}")
    public Boolean isSeatAvailable(@PathVariable int flightNumber);

}
