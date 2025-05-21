package com.search.services;

import com.search.clients.FlightServiceClient;
import com.search.entity.Flights;
import com.search.exceptions.FlightNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SearchServiceTest {

    @Mock
    private FlightServiceClient flightServiceClient;
    @InjectMocks
    private SearchService searchService;

    @Test
    void testGetAllFlights_Success() {
        Flights flight1 = new Flights(1, 1001, 4500.00, "Delhi", "Mumbai", LocalDate.of(2025, 5, 1));
        Flights flight2 = new Flights(2, 1002, 3500.00, "Bhopal", "Pune", LocalDate.of(2025, 5, 1));
        Flights flight3 = new Flights(3, 1003, 5500.00, "Delhi", "Mumbai", LocalDate.of(2025, 5, 1));

        when(flightServiceClient.getAllFlights()).thenReturn(Arrays.asList(flight1, flight2, flight3));

        List<Flights> result = searchService.getAllFlights("Delhi", "Mumbai", LocalDate.of(2025, 5, 1));

        assertEquals(2, result.size());
        assertEquals(flight1, result.get(0));
        assertEquals(flight3, result.get(1));
    }

    @Test
    void testGetAllFlights_NoFlightsFound_ShouldThrowException() {
        when(flightServiceClient.getAllFlights()).thenReturn(Collections.EMPTY_LIST);

        FlightNotFoundException exception = assertThrows(FlightNotFoundException.class, () ->
                searchService.getAllFlights("Delhi", "Mumbai", LocalDate.of(2025, 5, 1)));

        assertTrue(exception.getMessage().contains("No flights found from Delhi to Mumbai"));
    }

}
