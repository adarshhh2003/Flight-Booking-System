package com.flight.booking.service;

import com.flight.booking.clients.FlightServiceClient;
import com.flight.booking.clients.MessagingServiceClient;
import com.flight.booking.clients.PaymentServiceClient;
import com.flight.booking.dto.*;
import com.flight.booking.entity.Bookings;
import com.flight.booking.repository.BookingRepo;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    private BookingRepo bookingRepo;

    private FlightServiceClient flightServiceClient;

    private MessagingServiceClient messagingServiceClient;

    private PaymentServiceClient paymentServiceClient;

    private RabbitTemplate rabbitTemplate;

    public BookingService(BookingRepo bookingRepo, FlightServiceClient flightServiceClient,
                          MessagingServiceClient messagingServiceClient, PaymentServiceClient paymentServiceClient,
                          RabbitTemplate rabbitTemplate) {
        this.bookingRepo = bookingRepo;
        this.flightServiceClient = flightServiceClient;
        this.messagingServiceClient = messagingServiceClient;
        this.paymentServiceClient = paymentServiceClient;
        this.rabbitTemplate = rabbitTemplate;
    }

    public StripeResponse bookFlight(BookingDTO bookingDTO) {

        boolean checkSeatAvailable = flightServiceClient.isSeatAvailable(bookingDTO.getFlightNumber());

        if(!checkSeatAvailable) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Seat not available for flight number: " + bookingDTO.getFlightNumber());
        }

        Bookings pendingBooking = new Bookings(bookingDTO.getEmailId(), bookingDTO.getFlightNumber(),
                bookingDTO.getPassengerName(), bookingDTO.getBookingDate(),
                "Pending", null);

        bookingRepo.save(pendingBooking);

        try {

            Flights flights = flightServiceClient.getDetails(bookingDTO.getFlightNumber());

            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setAmount((int) flights.getFare()*100);
            paymentRequest.setCurrency("INR");
            paymentRequest.setFlightId(flights.getFlightNumber());
            paymentRequest.setBookingId(String.valueOf(bookingDTO.getId()));
            paymentRequest.setUserID(bookingDTO.getEmailId());

            StripeResponse paymentResponse = paymentServiceClient.checkoutProducts(paymentRequest);

            if(!"SUCCESS".equalsIgnoreCase(paymentResponse.getStatus())) {
                throw new IllegalStateException("Payment Failed" + paymentResponse.getMessage());
            }

            String seatNumber = "S" + flights.getAvailableSeats();

            pendingBooking.setStatus("CONFIRMED");
            pendingBooking.setSeatNumber(seatNumber);


            Bookings confirmedBooking = pendingBooking;

            bookingRepo.save(confirmedBooking);

            MessagingDetails messagingDetails = new MessagingDetails(
                    confirmedBooking.getEmailId(),
                    confirmedBooking.getPassengerName(),
                    Integer.toString(confirmedBooking.getId()),
                    Integer.toString(confirmedBooking.getFlightId()),
                    flights.getSource(),
                    flights.getDestination(),
                    confirmedBooking.getSeatNumber(),
                    flights.getFare(),
                    "CONFIRMED"
            );


            // Update the available seats
            flightServiceClient.updateAvailableSeats(flights.getFlightNumber(), flights.getAvailableSeats()-1);
            messagingServiceClient.sendMessage(messagingDetails);

            return paymentResponse;

        } catch (Exception e) {
            pendingBooking.setStatus("FAILED");
            Bookings failedBooking = pendingBooking;

            bookingRepo.save(failedBooking);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Booking Failed: " + e.getMessage());
        }
    }

    public void cancelBooking(int bookingId) {
        Optional<Bookings> bookings = bookingRepo.findById(bookingId);

        if(bookings.isPresent()) {
            Bookings exisitingBooking = bookings.get();
            exisitingBooking.setStatus("CANCELLED");
            bookingRepo.save(exisitingBooking);

            Flights flights = flightServiceClient.getDetails(exisitingBooking.getFlightId());

            MessagingDetails messagingDetails = new MessagingDetails(
                    exisitingBooking.getEmailId(),
                    exisitingBooking.getPassengerName(),
                    Integer.toString(exisitingBooking.getId()),
                    Integer.toString(exisitingBooking.getFlightId()),
                    flights.getSource(),
                    flights.getDestination(),
                    exisitingBooking.getSeatNumber(),
                    flights.getFare(),
                    "CANCELLED"
            );

            flightServiceClient.updateAvailableSeats(flights.getFlightNumber(), flights.getAvailableSeats()+1);
            messagingServiceClient.sendMessage(messagingDetails);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found with id: " + bookingId);
        }
    }

    public Bookings getBookingDetails(int id) {
        return bookingRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Booking not found with id: " + id));
    }

    public List<Bookings> getBookingsByUser(String email) {
        return bookingRepo.findByEmailId(email);
    }
}
