package ru.practicum.shareit.request.dto;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestDtoTest {
    private final JacksonTester<ItemRequestDto> json;
    private final ItemRequestMapper itemRequestMapper;

    @Test
    void testSerialize() throws Exception {
        User user = User.builder()
                .id(1L)
                .name("TestUserName")
                .email("UserEmail@test.com")
                .build();
        ItemRequest itemRequest = ItemRequest.builder()
                .id(1L)
                .requestor(user)
                .created(LocalDateTime.now())
                .description("TestItemRequestDescription")
                .build();
        Item item = Item.builder()
                .id(1L)
                .name("TestItemName")
                .description("TestItemDescription")
                .request(itemRequest)
                .available(true)
                .owner(user)
                .build();
        itemRequest.setItems(List.of(item));
        ItemRequestDto itemRequestDto = itemRequestMapper.toDto(itemRequest);

        JsonContent<ItemRequestDto> result = json.write(itemRequestDto);

        assertThat(result).hasJsonPath("$.id")
                .hasJsonPath("$.created")
                .hasJsonPath("$.description")
                .hasJsonPath("$.requestor.id")
                .hasJsonPath("$.requestor.name")
                .hasJsonPath("$.requestor.email")
                .hasJsonPath("$.items[0].id")
                .hasJsonPath("$.items[0].name")
                .hasJsonPath("$.items[0].description")
                .hasJsonPath("$.items[0].available");

        assertThat(result).extractingJsonPathNumberValue("$.id")
                .satisfies(id -> assertThat(id.longValue()).isEqualTo(itemRequestDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.created")
                .satisfies(created -> assertThat(created).isNotNull());
        assertThat(result).extractingJsonPathNumberValue("$.requestor.id")
                .satisfies(requestor_id -> assertThat(requestor_id.longValue()).isEqualTo(itemRequestDto.getId()));
        assertThat(result).extractingJsonPathStringValue("$.requestor.name")
                .satisfies(requestor_name -> assertThat(requestor_name).isEqualTo(itemRequestDto.getRequestor().getName()));
        assertThat(result).extractingJsonPathStringValue("$.requestor.email")
                .satisfies(requestor_email -> assertThat(requestor_email).isEqualTo(itemRequestDto.getRequestor().getEmail()));
    }
}