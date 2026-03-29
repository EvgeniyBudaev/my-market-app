package ru.practicum.market.web.dto;

public record Paging(
        int pageSize,
        int pageNumber,
        boolean hasPrevious,
        boolean hasNext
) {
}
