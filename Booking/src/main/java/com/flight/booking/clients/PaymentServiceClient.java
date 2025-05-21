package com.flight.booking.clients;

import com.flight.booking.dto.PaymentRequest;
import com.flight.booking.dto.StripeResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "Payment-Gateway")
public interface PaymentServiceClient {

    @PostMapping("/pay/checkout")
    public StripeResponse checkoutProducts(@RequestBody PaymentRequest paymentRequest);

}
