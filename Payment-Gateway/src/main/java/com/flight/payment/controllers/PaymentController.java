package com.flight.payment.controllers;

import com.flight.payment.dto.PaymentRequest;
import com.flight.payment.dto.StripeResponse;
import com.flight.payment.service.StripeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay")
public class PaymentController {

    private StripeService stripeService;

    @Autowired
    public PaymentController(StripeService stripeService) {
        this.stripeService = stripeService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody PaymentRequest paymentRequest) {

        StripeResponse stripeResponse = stripeService.checkoutProducts(paymentRequest);
        return ResponseEntity.ok(stripeResponse);

    }

}
