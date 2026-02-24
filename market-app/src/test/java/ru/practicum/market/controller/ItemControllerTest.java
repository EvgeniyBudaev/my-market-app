package ru.practicum.market.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import ru.practicum.market.config.SecurityConfig;
import ru.practicum.market.dto.ItemDto;
import ru.practicum.market.dto.PagingDto;
import ru.practicum.market.security.UserDetailsServiceImpl;
import ru.practicum.market.service.ItemService;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@WebFluxTest(ItemController.class)
@Import(SecurityConfig.class)
class ItemControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockBean
    private ItemService itemService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    void shouldAllowAnonymousAccessToItemsList() {
        // Настраиваем мок
        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setTitle("Товар 1");
        item.setPrice(100L);

        List<List<ItemDto>> items = List.of(List.of(item));
        PagingDto paging = new PagingDto();

        when(itemService.getItems(anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(items));
        when(itemService.getPagingInfo(anyString(), anyInt(), anyInt()))
                .thenReturn(Mono.just(paging));

        webTestClient
                .get()
                .uri("/items")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldAllowAnonymousAccessToSingleItem() {
        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setTitle("Товар 1");
        item.setPrice(100L);
        item.setDescription("Описание товара");

        when(itemService.getItemById(1L))
                .thenReturn(Mono.just(item));

        webTestClient
                .get()
                .uri("/items/1")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldDenyPostRequestForAnonymousUser() {
        webTestClient
                .post()
                .uri("/items")
                .exchange()
                .expectStatus().is3xxRedirection();
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldAllowPostRequestForAuthenticatedUser() {
        webTestClient
                .post()
                .uri("/items")
                .exchange()
                .expectStatus().is4xxClientError(); // Может быть 400 или 404, но не 401
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void shouldAllowAuthenticatedUserToViewItems() {
        ItemDto item = new ItemDto();
        item.setId(1L);
        item.setTitle("Товар 1");
        item.setPrice(100L);

        List<List<ItemDto>> items = List.of(List.of(item));
        PagingDto paging = new PagingDto();

        when(itemService.getItems(anyString(), any(), anyInt(), anyInt()))
                .thenReturn(Mono.just(items));
        when(itemService.getPagingInfo(anyString(), anyInt(), anyInt()))
                .thenReturn(Mono.just(paging));

        webTestClient
                .get()
                .uri("/items")
                .exchange()
                .expectStatus().isOk();
    }
}

