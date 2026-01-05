package ru.practicum.market.web.dto;

import java.util.List;

public record CartResponseDto(
        List<ItemResponseDto> items,
        long total
) {
    public CartResponseDto {
        items = List.copyOf(items);
    }
}
