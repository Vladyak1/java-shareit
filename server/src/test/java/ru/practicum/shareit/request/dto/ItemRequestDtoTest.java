package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    @Test
    @SneakyThrows
    public void itemRequestDtoSerialize() {
        User user = User.builder()
                .id(1L)
                .name("John Doe")
                .email("john.doe@example.com")
                .build();


        ItemRequestDto dto = ItemRequestDto.builder()
                .id(1L)
                .description("description")
                .requestor(user)
                .created(LocalDateTime.now())
                .build();
        JsonContent<ItemRequestDto> content = json.write(dto);
        assertThat(content)
                .hasJsonPath("$.id")
                .hasJsonPath("$.description")
                .hasJsonPath("$.requestor")
                .hasJsonPath("$.created");

        assertThat(content).extractingJsonPathNumberValue("@.id").isEqualTo(1);
        assertThat(content).extractingJsonPathStringValue("@.description").isEqualTo("description");
        assertThat(content).extractingJsonPathStringValue("@.requestor.name")
                .isEqualTo("John Doe");
    }
}