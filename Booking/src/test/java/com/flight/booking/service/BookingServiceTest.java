package com.flight.booking.service;

import com.flight.booking.clients.FlightServiceClient;
import com.flight.booking.clients.MessagingServiceClient;
import com.flight.booking.clients.PaymentServiceClient;
import com.flight.booking.dto.*;
import com.flight.booking.entity.Bookings;
import com.flight.booking.repository.BookingRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookingServiceTest {

    @Mock
    private BookingRepo bookingRepo;

    @Mock
    private FlightServiceClient flightServiceClient;

    @Mock
    private MessagingServiceClient messagingServiceClient;

    @Mock
    private PaymentServiceClient paymentServiceClient;

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private BookingService bookingService;

    private BookingDTO bookingDTO;
    private Flights mockFlight;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        bookingDTO = new BookingDTO();
        bookingDTO.setEmailId("adarshpatel@gmail.com");
        bookingDTO.setFlightNumber(1001);
        bookingDTO.setPassengerName("Adarsh Patel");
        bookingDTO.setBookingDate(LocalDate.now());
        bookingDTO.setStatus("Pending");
        bookingDTO.setId(1);

        mockFlight = new Flights();
        mockFlight.setFlightNumber(1001);
        mockFlight.setFare(4500.00);
        mockFlight.setAvailableSeats(5);
        mockFlight.setSource("Delhi");
        mockFlight.setDestination("Mumbai");
        mockFlight.setFlightDate(LocalDate.now());
    }

    @Test
    void testBookFlight_Success() {
        when(flightServiceClient.isSeatAvailable(1001)).thenReturn(true);
        when(bookingRepo.save(any(Bookings.class))).thenReturn(new Bookings());
        when(flightServiceClient.getDetails(1001)).thenReturn(mockFlight);

        StripeResponse mockResponse = StripeResponse.builder()
                .status("Success")
                .message("Payment Done")
                .build();

        when(paymentServiceClient.checkoutProducts(any(PaymentRequest.class))).thenReturn(mockResponse);

        StripeResponse response = bookingService.bookFlight(bookingDTO);

        assertNotNull(response);
        assertEquals("Success", response.getStatus());

        verify(bookingRepo, times(2)).save(any(Bookings.class));
        verify(flightServiceClient).updateAvailableSeats(1001, 4);
        verify(messagingServiceClient).sendMessage(any(MessagingDetails.class));
    }

    @Test
    void testBookFlight_SeatNotAvailable_ThrowsException() {
        when(flightServiceClient.isSeatAvailable(1001)).thenReturn(false);


        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                bookingService.bookFlight(bookingDTO));

        assertTrue(exception.getMessage().contains("Seat not available for flight number: 1001"));
        verify(bookingRepo, never()).save(any(Bookings.class));
    }

    @Test
    void testBookFlight_PaymentFails_ThrowsException() {
        when(flightServiceClient.isSeatAvailable(1001)).thenReturn(true);
        when(bookingRepo.save(any(Bookings.class))).thenReturn(new Bookings());
        when(flightServiceClient.getDetails(1001)).thenReturn(mockFlight);

        StripeResponse failResponse = StripeResponse.builder()
                .status("Failed")
                .message("Card Declined")
                .build();

        when(paymentServiceClient.checkoutProducts(any(PaymentRequest.class))).thenReturn(failResponse);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () ->
                bookingService.bookFlight(bookingDTO));

        assertTrue(exception.getMessage().contains("Booking Failed"));
        verify(bookingRepo, atLeast(2)).save(any(Bookings.class));
    }

    @Test
    void testGetBookingDetails_Success() {
        Bookings bookings = new Bookings("adarshpatel0191@gmail.com", 1001, "Adarsh Patel",
                LocalDate.now(), "CONFIRMED", "S28");
        bookings.setId(12);

        when(bookingRepo.findById(12)).thenReturn(Optional.of(bookings));

        Bookings result = bookingService.getBookingDetails(12);

        assertNotNull(result);
        assertEquals("CONFIRMED", result.getStatus());
        assertEquals("adarshpatel0191@gmail.com", result.getEmailId());
    }

    @Test
    void testGetBookingDetails_NotFound() {
        when(bookingRepo.findById(12)).thenReturn(Optional.empty());

        ResponseStatusException exception =
                assertThrows(ResponseStatusException.class, () -> bookingService.getBookingDetails(12));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertTrue(exception.getReason().contains("Booking not found"));
    }

    @Test
    void testCancelBooking() {
        Bookings bookings = new Bookings("adarshpatel0191@gmail.com", 1001, "Adarsh Patel",
                LocalDate.now(), "CONFIRMED", "S28");
        bookings.setId(5);

        Flights flight = new Flights(1001, 4500.00,"Delhi", "Mumbai",
                LocalDate.of(2025, 5, 12), 50, 10);

        when(bookingRepo.findById(5)).thenReturn(Optional.of(bookings));
        when(flightServiceClient.getDetails(1001)).thenReturn(flight);

        bookingService.cancelBooking(5);

        verify(flightServiceClient).updateAvailableSeats(1001, 11);
        verify(messagingServiceClient).sendMessage(any(MessagingDetails.class));
    }

}
