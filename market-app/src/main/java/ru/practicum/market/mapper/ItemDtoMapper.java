package ru.practicum.market.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.market.dto.ItemDto;
import ru.practicum.market.model.CartItem;
import ru.practicum.market.model.Item;
import ru.practicum.market.model.OrderItem;

@Component
public class ItemDtoMapper {
    public ItemDto fromItem(Item item, int count) {
        return new ItemDto(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getImgPath(),
                item.getPrice(),
                count
        );
    }

    public ItemDto fromCartItem(CartItem cartItem) {
        Item item = cartItem.getItem();
        return new ItemDto(
                item.getId(),
                item.getTitle(),
                item.getDescription(),
                item.getImgPath(),
                item.getPrice(),
                cartItem.getCount()
        );
    }

    public ItemDto fromOrderItem(OrderItem orderItem) {
        Item item = orderItem.getItem();
        return new ItemDto(
                item.getId(),
                item.getTitle(),
                null,
                null,
                orderItem.getPrice(),
                orderItem.getCount()
        );
    }
}
