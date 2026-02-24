package ru.practicum.market.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.market.dto.OrderDto;

public interface OrderService {
    Mono<Long> createOrder();
    Flux<OrderDto> getAllOrders();
    Mono<OrderDto> getOrderById(Long id);
}
