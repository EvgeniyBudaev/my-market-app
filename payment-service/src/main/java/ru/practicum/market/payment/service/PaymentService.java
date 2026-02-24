package ru.practicum.market.payment.service;

import reactor.core.publisher.Mono;

public interface PaymentService {
    Mono<Long> getBalance(String username);

    Mono<PaymentResult> processPayment(String username, Long amount, Long orderId, String description);
}

