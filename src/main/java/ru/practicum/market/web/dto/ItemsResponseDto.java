package ru.practicum.market.web.dto;

import ru.practicum.market.web.dto.enums.SortMethod;

import java.util.List;

public record ItemsResponseDto(
        List<List<ItemResponseDto>> items,
        String search,
        SortMethod sort,
        Paging paging
) {
    public ItemsResponseDto {
        items = List.copyOf(items);
    }
}
