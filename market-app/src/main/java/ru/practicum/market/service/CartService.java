package ru.practicum.market.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.market.dto.ItemDto;
import ru.practicum.market.enums.ItemAction;

public interface CartService {
    Flux<ItemDto> getCartItems();
    Mono<Long> getTotalPrice();
    Mono<Void> updateCartItem(Long itemId, ItemAction action);
    Mono<Void> clearCart();
}
