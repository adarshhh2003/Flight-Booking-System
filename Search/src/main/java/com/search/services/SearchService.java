package com.search.services;

import com.search.clients.FlightServiceClient;
import com.search.entity.Flights;
import com.search.exceptions.FlightNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SearchService {
    private FlightServiceClient flightServiceClient;

    @Autowired
    public SearchService(FlightServiceClient flightServiceClient) {
        this.flightServiceClient = flightServiceClient;
    }

    public List<Flights> getAllFlights(String source, String destination, LocalDate flightDate) {
        List<Flights> allFlights = flightServiceClient.getAllFlights();
        List<Flights> filteredFlights = allFlights.stream()
                .filter(flights ->
                        flights.getSource().equalsIgnoreCase(source) &&
                        flights.getDestination().equalsIgnoreCase(destination) &&
                        flights.getFlightDate().equals(flightDate)).toList();

        if(filteredFlights.isEmpty()) {
            throw new FlightNotFoundException("No flights found from " + source + " to " + destination
                    + " on " + flightDate);
        }

        return filteredFlights;
    }


    public List<Flights> getAllAvailableFlights() {
        List<Flights> allFlights = flightServiceClient.getAllFlights();

        if(allFlights.isEmpty()) {
            throw new FlightNotFoundException("FLights are not available");
        }

        return allFlights;
    }
}
