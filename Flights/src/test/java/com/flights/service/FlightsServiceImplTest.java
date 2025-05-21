package com.flights.service;

import com.flights.entity.Flights;
import com.flights.exceptions.FlightNotFoundException;
import com.flights.repository.FlightsRepo;
import com.flights.services.FlightsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;


public class FlightsServiceImplTest {

    @Mock
    private FlightsRepo flightsRepo;

    @InjectMocks
    private FlightsServiceImpl flightService;

    private Flights flight1, flight2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        flight1 = new Flights(1, 1001, 4500.00, "Delhi", "Mumbai",
                LocalDate.of(2025, 5,1),50, 20);
        flight2 = new Flights(2, 1002, 3500.00, "Pune", "Bhopal",
                LocalDate.of(2025, 5,2), 50, 0);

    }

    @Test
    void testAddFlights_Success() {
        when(flightsRepo.save(flight1)).thenReturn(flight1);
        Flights saved = flightService.addFlights(flight1);
        assertEquals(flight1, saved);
        verify(flightsRepo).save(flight1);
    }

    @Test
    void testGetAllFlight_Success() {
        when(flightsRepo.findAll()).thenReturn(List.of(flight1, flight2));
        List<Flights> flightsList = flightService.getAllFlights();
        assertEquals(2, flightsList.size());
        verify(flightsRepo).findAll();
    }

    @Test
    void testGetAllFlights_Empty_ShouldThrowException() {
        when(flightsRepo.findAll()).thenReturn(Collections.EMPTY_LIST);
        assertThrows(FlightNotFoundException.class,() -> flightService.getAllFlights());
    }

    @Test
    void testGetFlightsSortedByPrice() {
        when(flightsRepo.findAll()).thenReturn(List.of(flight1, flight2));
        List<Flights> sorted = flightService.getFlightsSortedByPrice();
        assertEquals(flight1, sorted.get(1));
        assertEquals(flight2, sorted.get(0));
        verify(flightsRepo).findAll();
    }

    @Test
    void testUpdateFlights_Success() {
        Flights update = new Flights();
        update.setAvailableSeats(99);
        update.setFare(9999.00);

        when(flightsRepo.findByFlightNumber(1001)).thenReturn(Optional.of(flight1));
        when(flightsRepo.save(any(Flights.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Flights updated = flightService.updateFlights(1001, update);

        assertEquals(9999.00, updated.getFare());
        assertEquals(1001, updated.getFlightNumber());
    }

    @Test
    void testDeleteFlights_Success() {
        when(flightsRepo.deleteByFlightNumber(1001)).thenReturn(1);
        assertDoesNotThrow(()-> flightService.deleteFlights(1001));
    }

    @Test
    void deleteFlights_NotFound() {
        when(flightsRepo.deleteByFlightNumber(1005)).thenReturn(0);
        assertThrows(FlightNotFoundException.class, () -> flightService.deleteFlights(1005));
    }

    @Test
    void testUpdateAvailableSeats_Success() {
        when(flightsRepo.findByFlightNumber(1001)).thenReturn(Optional.ofNullable(flight1));
        Flights updated = flightService.updateAvailableSeats(1001, 77);
        assertEquals(77, updated.getAvailableSeats());
    }

    @Test
    void testIsSeatAvailable_True() {
        when(flightsRepo.findByFlightNumber(1001)).thenReturn(Optional.ofNullable(flight1));
        assertTrue(flightService.isSeatAvailable(1001));
    }

    @Test
    void testIsSeatAvailable_False() {
        when(flightsRepo.findByFlightNumber(1002)).thenReturn(Optional.ofNullable(flight2));
        assertFalse(flightService.isSeatAvailable(1002));
    }

    @Test
    void testIsSeatAvailable_FlightNotFound() {
        when(flightsRepo.findByFlightNumber(1005)).thenReturn(Optional.empty());
        assertThrows(FlightNotFoundException.class, () -> flightService.isSeatAvailable(1005));
    }
}
