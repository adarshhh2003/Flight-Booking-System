package com.flights.services;

import com.flights.entity.Flights;

import java.util.List;

public interface FlightsService {
    public Flights addFlights(Flights flights);
    public List<Flights> getAllFlights();
    public Flights updateFlights(int flightNumber, Flights flights);
    public void deleteFlights(int FlightNumber);
}
