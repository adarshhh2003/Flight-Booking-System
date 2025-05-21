package com.flight.message.service;

import com.flight.message.dto.MessagingDetails;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendBookingEmail(MessagingDetails details) {

        System.out.println(details);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(details.getRecipientEmail());
        message.setSubject("Booking Confirmation - " + details.getBookingId());
        message.setText("Dear " + details.getPassengerName() + ",\n\n"
                + "Your booking (ID: " + details.getBookingId() + ") for Flight " + details.getFlightId()
                + " from " + details.getSource() + " to " + details.getDestination()
                + " has been confirmed.\n\n"
                + "Seat Number : " + details.getSeatNumber() + "\n"
                + "Total Amount Paid: Rs" + details.getTotalAmountPaid() + "\n\n"
                + "Thankyou for choosing our service!"
        );

        javaMailSender.send(message);

    }

    public void sendCancelEmail(MessagingDetails details) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(details.getRecipientEmail());
        message.setSubject("Booking Cancellation: " + details.getBookingId());
        message.setText("Dear " + details.getPassengerName() + "\n\n"
                + "We regret to inform you that your booking (Id: " + details.getBookingId() + ") for Flight " + details.getFlightId()
                + "from " + details.getSource() + " to " + details.getDestination()
                + " has been cancelled.\n\n"
                + "Seat Number: " + details.getSeatNumber() + "\n"
                + "Refunded Amount (if applicable): Rs" + details.getTotalAmountPaid() + "\n\n"
                + "If you have any questions or need further assistance, feel free to contact us.\n\n"
                + "We hope to serve you again soon!"
        );

        javaMailSender.send(message);
    }

}
