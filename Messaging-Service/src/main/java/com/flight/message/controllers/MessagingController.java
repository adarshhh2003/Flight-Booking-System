package com.flight.message.controllers;

import com.flight.message.config.RabbitMQConfig;
import com.flight.message.dto.MessagingDetails;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/message")
public class MessagingController {

    private RabbitTemplate rabbitTemplate;

    private RabbitMQConfig rabbitMQConfig;

    public MessagingController(RabbitTemplate rabbitTemplate, RabbitMQConfig rabbitMQConfig) {
        this.rabbitTemplate = rabbitTemplate;
        this.rabbitMQConfig = rabbitMQConfig;
    }

    @PostMapping("/notify")
    public ResponseEntity<String> sendMessage(@RequestBody MessagingDetails details) {
        rabbitTemplate.convertAndSend(rabbitMQConfig.getQueueName(), details);
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }
}
