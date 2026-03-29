package ru.practicum.market.web.dto;

public record ItemResponseDto(
        long id,
        String title,
        String description,
        String imgPath,
        long price,
        int count
) {
}
