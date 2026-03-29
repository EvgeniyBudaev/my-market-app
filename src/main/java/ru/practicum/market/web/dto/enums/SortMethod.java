package ru.practicum.market.web.dto.enums;

import lombok.Getter;

@Getter
public enum SortMethod {
    NO(null),
    ALPHA("title"),
    PRICE("price");

    private final String columnName;

    SortMethod(String columnName) {
        this.columnName = columnName;
    }
}
