package ru.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.practicum.market.model.User;
import ru.practicum.market.repository.UserRepository;
import ru.practicum.market.security.SecurityUtils;
import ru.practicum.market.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;

    @Override
    public Mono<Long> getCurrentUserId() {
        return getCurrentUser()
                .map(User::getId);
    }

    @Override
    public Mono<User> getCurrentUser() {
        return SecurityUtils.getCurrentUsername()
                .flatMap(userRepository::findByUsername);
    }
}

