package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    public ItemRequestDto getItemRequestDto() {
        return ItemRequestDto.builder()
                .id(1L)
                .description("TestItemRequestDescription")
                .requestor(null)
                .created(LocalDateTime.now())
                .build();
    }

    @Test
    void findAll() throws Exception {
        when(itemRequestService.getItemRequests(anyLong())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
        verify(itemRequestService, times(1)).getItemRequests(anyLong());
    }

}