package com.techmatrix18.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.format.DateTimeFormatter;

/**
 * PaymentController
 *
 * @author Alexander Kuziv <makklays@gmail.com>
 * @company TechMatrix18
 * @version 0.0.1
 * @since 03.11.2026
 */

@RestController
@RequestMapping("/aggregate/v1/payments")
public class PaymentController {
    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private final WebClient webClient;

    public PaymentController(
            @Value("${microservices.payments-url}") String paymentServiceUrl,
            WebClient.Builder webClient
    ) {
        this.webClient = webClient.baseUrl(paymentServiceUrl).build();
    }

    /*@GetMapping("/base-currencies/{code}/propagate")
    public ResponseEntity<Map<String, Object>> propagatePaymentUpdate(
            @PathVariable String code,
            @RequestBody Map<String, Object> incomingPrices
    ) {
        //
    }*/
}

