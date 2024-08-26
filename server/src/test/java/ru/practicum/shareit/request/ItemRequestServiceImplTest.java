package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestResponseDto;
import ru.practicum.shareit.request.mapper.ItemRequestListMapper;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class ItemRequestServiceImplTest {

    private ItemRequestService itemRequestService;
    private ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private User user;
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private Pageable pageable;
    private final ItemRequestMapper itemRequestMapper;
    private final ItemRequestListMapper itemRequestListMapper;

    @BeforeEach
    void setUp() {
        itemRequestRepository = mock(ItemRequestRepository.class);
        itemRequestService = new ItemRequestServiceImpl(
                itemRequestRepository, userRepository, itemRequestMapper, itemRequestListMapper);
        user = User.builder()
                .id(1L)
                .name("TestUserName")
                .email("UserEmail@test.com")
                .build();
        userRepository.save(user);
        itemRequest = ItemRequest.builder()
                .id(1L)
                .requestor(user)
                .created(LocalDateTime.now())
                .description("TestItemRequestDescription")
                .build();
        itemRequestDto = itemRequestMapper.toDto(itemRequest);
//        itemRequestDto = new ItemRequestDto(user.getId(), "TestItemRequestText");

        pageable = PageRequest.of(0, 10, Sort.by("created").descending());
        final int start = (int) pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), List.of(itemRequest).size());
        final Page<ItemRequest> page = new PageImpl<>(List.of(itemRequest).subList(start, end), pageable, List.of(itemRequest).size());
        when(itemRequestRepository.findAll((Pageable) any())).thenReturn(page);

        when(itemRequestRepository.save(any())).thenReturn(itemRequest);
    }

    @Test
    void findAllByUserId() {
        List<ItemRequestResponseDto> result = itemRequestService.getItemRequests(user.getId());
        Assertions.assertNotNull(result);
        Assertions.assertEquals(result.getFirst().getCreated(), itemRequestDto.getCreated());
        Assertions.assertEquals(result.getFirst().getDescription(), itemRequestDto.getDescription());
        Assertions.assertEquals(result.getFirst().getRequestor(), itemRequestDto.getRequestor());
        Assertions.assertEquals(result.getFirst().getId(), itemRequestDto.getId());
    }
}