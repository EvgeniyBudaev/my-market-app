package ru.practicum.market.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Заказ не найден с id: " + id);
    }
}

