package ru.practicum.market.web.dto;

import java.util.List;

public record OrderResponseDto(
        long id,
        List<ItemResponseDto> items,
        long totalSum
) {
    public OrderResponseDto {
        items = List.copyOf(items);
    }
}
