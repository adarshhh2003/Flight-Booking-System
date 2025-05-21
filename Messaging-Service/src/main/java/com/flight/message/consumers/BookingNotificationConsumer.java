package com.flight.message.consumers;

import com.flight.message.dto.MessagingDetails;
import com.flight.message.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BookingNotificationConsumer {

    private EmailService emailService;

    public BookingNotificationConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "booking_notification_queue")
    public void receiveMessage(MessagingDetails details) {

        try {
            System.out.println("Message receive in the consumer");
            if ("CONFIRMED".equalsIgnoreCase(details.getStatus())) {
                emailService.sendBookingEmail(details);
            } else {
                emailService.sendCancelEmail(details);
            }
        } catch (Exception e) {
            System.out.println("Exception in the consumer");
            e.printStackTrace();
        }
    }
}
