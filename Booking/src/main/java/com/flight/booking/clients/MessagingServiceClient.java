package com.flight.booking.clients;

import com.flight.booking.dto.MessagingDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Messaging-Service")
public interface MessagingServiceClient {

    @PostMapping("/message/notify")
    public String sendMessage(@RequestBody MessagingDetails details);

}
