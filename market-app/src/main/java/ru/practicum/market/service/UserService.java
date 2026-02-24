package ru.practicum.market.service;

import reactor.core.publisher.Mono;
import ru.practicum.market.model.User;

public interface UserService {
    /**
     * Получить ID текущего аутентифицированного пользователя
     */
    Mono<Long> getCurrentUserId();
    
    /**
     * Получить текущего аутентифицированного пользователя
     */
    Mono<User> getCurrentUser();
}

