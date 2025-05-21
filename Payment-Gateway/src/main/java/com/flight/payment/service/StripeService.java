package com.flight.payment.service;

import com.flight.payment.dto.PaymentRequest;
import com.flight.payment.dto.StripeResponse;
import com.flight.payment.entity.Payment;
import com.flight.payment.repository.PaymentRepo;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StripeService {

    @Value("${stripe.secretKey}")
    private String secretKey;

    private PaymentRepo paymentRepo;

    @Autowired
    public StripeService(PaymentRepo paymentRepo) {
        this.paymentRepo = paymentRepo;
    }

    public StripeResponse checkoutProducts(PaymentRequest paymentRequest) {
        Stripe.apiKey = secretKey;

        // Prepare product and price data
        SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                        .setName("Flight Id: " + paymentRequest.getFlightId())
                        .build();

        SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                        .setCurrency(paymentRequest.getCurrency() != null ? paymentRequest.getCurrency() : "INR")
                        .setUnitAmount((long) paymentRequest.getAmount())
                        .setProductData(productData)
                        .build();

        SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                        .setPriceData(priceData)
                        .setQuantity(1L)
                        .build();

        SessionCreateParams params =
                SessionCreateParams.builder()
                        .setMode(SessionCreateParams.Mode.PAYMENT)
                        .setSuccessUrl("http://localhost:8085/success")
                        .setCancelUrl("http://localhost:8085/cancel")
                        .addLineItem(lineItem)
                        .build();


        try {

            Session session = Session.create(params);

            Payment payment = Payment.builder()
                    .bookingId(paymentRequest.getBookingId())
                    .stripeSessionId(session.getId())
                    .currency(paymentRequest.getCurrency())
                    .amount(paymentRequest.getAmount())
                    .quantity(1)
                    .productName("Flight Id: " + paymentRequest.getFlightId())
                    .status("CREATED")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();

            paymentRepo.save(payment);

            return StripeResponse.builder()
                    .status("SUCCESS")
                    .message("Payment Session Created")
                    .sessionId(session.getId())
                    .sessionUrl(session.getUrl())
                    .build();

        } catch (StripeException e) {

            e.printStackTrace();
            return StripeResponse.builder()
                    .status("FAILED")
                    .message("Stripe Session Creation Failed: " + e.getMessage())
                    .build();

        }

    }
}
