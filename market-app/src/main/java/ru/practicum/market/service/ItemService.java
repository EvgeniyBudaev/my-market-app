package ru.practicum.market.service;

import reactor.core.publisher.Mono;
import ru.practicum.market.dto.ItemDto;
import ru.practicum.market.dto.PagingDto;
import ru.practicum.market.enums.ItemAction;
import ru.practicum.market.enums.SortType;

import java.util.List;

public interface ItemService {
    Mono<List<List<ItemDto>>> getItems(String search, SortType sort, int pageNumber, int pageSize);
    Mono<ItemDto> getItemById(Long id);
    Mono<Void> updateCartItem(Long itemId, ItemAction action);
    Mono<PagingDto> getPagingInfo(String search, int pageNumber, int pageSize);
}
