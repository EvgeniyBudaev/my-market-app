package ru.practicum.market.exception;

public class EmptyCartException extends RuntimeException {
    public EmptyCartException() {
        super("Корзина пуста");
    }
}

