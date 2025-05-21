package com.flights.services;

import com.flights.entity.Flights;
import com.flights.exceptions.FlightNotFoundException;
import com.flights.repository.FlightsRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlightsServiceImpl implements FlightsService{

    private FlightsRepo flightsRepo;

    @Autowired
    public FlightsServiceImpl(FlightsRepo flightsRepo) {
        this.flightsRepo = flightsRepo;
    }

    @Override
    public Flights addFlights(Flights flights) {
        System.out.println(flights.getFlightDate());
        return flightsRepo.save(flights);
    }

    @Override
    public List<Flights> getAllFlights() {
        List<Flights> allFlights = flightsRepo.findAll();

        if(allFlights.isEmpty()) {
            throw new FlightNotFoundException("No flights are available");
        }
        return allFlights;
    }

    public List<Flights> getFlightsSortedByPrice() {
        List<Flights> allFlights = flightsRepo.findAll();

        if(allFlights.isEmpty()) {
            throw new FlightNotFoundException("No flights are available");
        }

        return allFlights.stream()
                .sorted(Comparator.comparingDouble(Flights::getFare))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Flights updateFlights(int flightNumber, Flights updateFlights) {
        Flights existingFlight = flightsRepo.findByFlightNumber(flightNumber)
                .orElseThrow(() -> new FlightNotFoundException("Flight not found with number: " + flightNumber));

        BeanUtils.copyProperties(updateFlights, existingFlight, "flightNumber", "id");
        return flightsRepo.save(existingFlight);
    }

    @Override
    @Transactional
    public void deleteFlights(int flightNumber) {
        int deleteCount = flightsRepo.deleteByFlightNumber(flightNumber);
        if(deleteCount==0) {
            throw new FlightNotFoundException("Flight not found with number: " + flightNumber);
        }
    }

    public Flights updateAvailableSeats(int flightNumber, int availableSeats) {
        Optional<Flights> flights = flightsRepo.findByFlightNumber(flightNumber);

        if(flights.isEmpty()) {
            throw new FlightNotFoundException("Flight not found with id: " + flightNumber);
        }

        flights.get().setAvailableSeats(availableSeats);
        flightsRepo.save(flights.get());
        return flights.get();
    }

    public Flights getDetails(int flightNumber) {
        Optional<Flights> flights = flightsRepo.findByFlightNumber(flightNumber);

        if(flights.isEmpty()) {
            throw new FlightNotFoundException("Flight not found with flightNumber: " + flightNumber);
        }

        return flights.get();
    }

    public boolean isSeatAvailable(int flightNumber) {
        Optional<Flights> flights = flightsRepo.findByFlightNumber(flightNumber);

        if(flights.isEmpty()) {
            throw new FlightNotFoundException("Flight not found with flightNumber: " + flightNumber);
        }
        System.out.println(flights.get());

        return flights.get().getAvailableSeats()>0;
    }
}
