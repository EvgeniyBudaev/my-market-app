package ru.practicum.market.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.practicum.market.dto.ItemDto;
import ru.practicum.market.dto.PagingDto;
import ru.practicum.market.enums.ItemAction;
import ru.practicum.market.enums.SortType;
import ru.practicum.market.exception.ItemNotFoundException;
import ru.practicum.market.mapper.ItemDtoMapper;
import ru.practicum.market.model.CartItem;
import ru.practicum.market.model.Item;
import ru.practicum.market.repository.CartItemRepository;
import ru.practicum.market.repository.ItemRepository;
import ru.practicum.market.service.CartService;
import ru.practicum.market.service.ItemCacheService;
import ru.practicum.market.service.ItemService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemRepository itemRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemDtoMapper itemDtoMapper;
    private final CartService cartService;
    private final ItemCacheService itemCacheService;

    @Override
    public Mono<List<List<ItemDto>>> getItems(String search, SortType sort, int pageNumber, int pageSize) {
        Flux<Item> itemsFlux;

        if (search != null && !search.isEmpty()) {
            itemsFlux = itemRepository.findByTitleOrDescriptionContaining(search);
        } else {
            itemsFlux = itemCacheService.getAllItemsFromCache()
                    .switchIfEmpty(
                        itemRepository.findAll()
                            .collectList()
                            .flatMapMany(items ->
                                itemCacheService.saveAllItemsToCache(items)
                                    .thenMany(Flux.fromIterable(items))
                            )
                    );
        }

        return getCartCounts()
                .flatMap(cartCounts ->
                    itemsFlux
                        .sort(getComparator(sort))
                        .map(item -> itemDtoMapper.fromItem(item, cartCounts.getOrDefault(item.getId(), 0)))
                        .collectList()
                        .map(itemDtos -> {
                            int startIndex = Math.max(0, (pageNumber - 1) * pageSize);
                            if (startIndex >= itemDtos.size()) {
                                return Collections.<ItemDto>emptyList();
                            }
                            int endIndex = Math.min(startIndex + pageSize, itemDtos.size());
                            return itemDtos.subList(startIndex, endIndex);
                        })
                        .map(this::groupByThreeWithPlaceholders)
                );
    }

    @Override
    public Mono<ItemDto> getItemById(Long id) {
        return itemCacheService.getItemFromCache(id)
                .switchIfEmpty(
                    itemRepository.findById(id)
                        .flatMap(itemCacheService::saveItemToCache)
                )
                .switchIfEmpty(Mono.error(new ItemNotFoundException(id)))
                .zipWith(getCartCounts())
                .map(tuple -> {
                    Item item = tuple.getT1();
                    Map<Long, Integer> cartCounts = tuple.getT2();
                    int count = cartCounts.getOrDefault(id, 0);
                    return itemDtoMapper.fromItem(item, count);
                });
    }

    @Override
    public Mono<Void> updateCartItem(Long itemId, ItemAction action) {
        return itemRepository.findById(itemId)
                .switchIfEmpty(Mono.error(new ItemNotFoundException(itemId)))
                .flatMap(item -> cartService.updateCartItem(itemId, action));
    }

    @Override
    public Mono<PagingDto> getPagingInfo(String search, int pageNumber, int pageSize) {
        Mono<Long> totalItemsMono = (search != null && !search.isEmpty())
                ? itemRepository.findByTitleOrDescriptionContaining(search).count()
                : itemRepository.count();

        return totalItemsMono.map(totalItems -> {
            int totalPages = (int) Math.ceil((double) totalItems / pageSize);
            boolean hasPrevious = pageNumber > 1;
            boolean hasNext = pageNumber < totalPages;
            return new PagingDto(pageSize, pageNumber, hasPrevious, hasNext);
        });
    }

    private Comparator<Item> getComparator(SortType sort) {
        if (sort == SortType.ALPHA) {
            return Comparator.comparing(Item::getTitle);
        } else if (sort == SortType.PRICE) {
            return Comparator.comparing(Item::getPrice);
        }
        return (a, b) -> 0;
    }

    private Mono<Map<Long, Integer>> getCartCounts() {
        return cartItemRepository.findAll()
                .collectMap(CartItem::getItemId, CartItem::getCount);
    }

    private List<List<ItemDto>> groupByThreeWithPlaceholders(List<ItemDto> items) {
        List<List<ItemDto>> grouped = new ArrayList<>();
        for (int i = 0; i < items.size(); i += 3) {
            List<ItemDto> row = new ArrayList<>(3);
            for (int j = 0; j < 3; j++) {
                int idx = i + j;
                if (idx < items.size()) {
                    row.add(items.get(idx));
                } else {
                    row.add(new ItemDto(-1L, "", "", "", 0L, 0));
                }
            }
            grouped.add(row);
        }
        return grouped;
    }
}

