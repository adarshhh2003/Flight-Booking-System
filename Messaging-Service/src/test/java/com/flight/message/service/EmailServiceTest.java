package com.flight.message.service;

import com.flight.message.dto.MessagingDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    private JavaMailSender javaMailSender;
    private EmailService emailService;

    @BeforeEach
    void setUp() {
        javaMailSender = mock(JavaMailSender.class);
        emailService = new EmailService(javaMailSender);
    }

    @Test
    void testSendBookingEmail() {
        MessagingDetails details = new MessagingDetails(
                "adarsh@gmail.com", "Adarsh Patel", "B123", "F456",
                "Delhi", "Mumbai", "S12", 8000, "CONFIRMED"
        );

        emailService.sendBookingEmail(details);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertEquals("adarsh@gmail.com", message.getTo()[0]);
        assertTrue(message.getSubject().contains("Booking Confirmation"));
        assertTrue(message.getText().contains("Adarsh Patel"));
        assertTrue(message.getText().contains("Flight F456"));
    }

    @Test
    void testSendCancelEmail() {
        MessagingDetails details = new MessagingDetails(
                "adarsh@gmail.com", "Adarsh Patel", "B123", "F456",
                "Delhi", "Mumbai", "S12", 8000, "CANCELLED"
        );

        emailService.sendCancelEmail(details);

        ArgumentCaptor<SimpleMailMessage> captor = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(javaMailSender, times(1)).send(captor.capture());

        SimpleMailMessage message = captor.getValue();
        assertEquals("adarsh@gmail.com", message.getTo()[0]);
        assertTrue(message.getSubject().contains("Booking Cancellation"));
        assertTrue(message.getText().contains("Adarsh Patel"));
        assertTrue(message.getText().contains("Flight F456"));
    }
}

