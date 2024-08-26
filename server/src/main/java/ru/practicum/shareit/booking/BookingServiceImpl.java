package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.exception.BookingServiceException;
import ru.practicum.shareit.exception.InterruptionRuleException;
import ru.practicum.shareit.exception.MyNotFoundException;
import ru.practicum.shareit.exception.RepositoryReceiveException;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.mapper.BookingMapper;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BookingServiceImpl implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;
    private final BookingMapper bookingMapper;

    @Override
    public BookingDto saveBooking(Long userId, BookingRequestDto bookingRequestDto) {
        Item item = itemRepository.findById(bookingRequestDto.getItemId())
                .orElseThrow(() -> new MyNotFoundException("Такой вещи для бронирования нет"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new MyNotFoundException("Пользователь с id = " + userId + " не найден"));
        if (!item.getAvailable()) {
            throw new BookingServiceException("Вещь недоступна для бронирования");
        }
        return bookingMapper.toDto(bookingRepository.save(
                Booking.builder()
                        .start(bookingRequestDto.getStart())
                        .end(bookingRequestDto.getEnd())
                        .item(item)
                        .booker(user)
                        .status(BookingStatus.WAITING)
                        .build()
        ));
    }

    @Override
    public BookingDto setApproved(Long userId, Long bookingId, Boolean approved) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RepositoryReceiveException("Такого бронирования нет"));
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new RepositoryReceiveException("Такой вещи для бронирования нет"));
        if (!item.getOwner().getId().equals(userId)) {
            throw new BookingServiceException("Подтверждать бронирование может только владелец");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new InterruptionRuleException("Вещь уже забронирована");
        }
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        } else {
            booking.setStatus(BookingStatus.REJECTED);
        }
        bookingRepository.save(booking);
        return bookingMapper.toDto(booking);
    }

    @Override
    public BookingDto getBooking(Long bookingId, Long userId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RepositoryReceiveException("Такого бронирования нет"));
        Item item = itemRepository.findById(booking.getItem().getId())
                .orElseThrow(() -> new RepositoryReceiveException("Такой вещи для бронирования нет"));
        if (!booking.getBooker().getId().equals(userId) && !item.getOwner().getId().equals(userId)) {
            throw new BookingServiceException("Запрашивать бронирование можен только владелец, " +
                    "либо забронировавший пользователь");
        }
        return bookingMapper.toDto(booking);
    }

    @Override
    public List<BookingDto> getAllByBookerAndStatus(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new MyNotFoundException("Пользователь не найден"));
        return switch (BookingState.from(state).get()) {
            case ALL -> bookingRepository.findAllByBookerIdOrderByStartDesc(userId).stream()
                    .map(bookingMapper::toDto)
                    .toList();
            case CURRENT ->
                    bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now()).stream()
                            .map(bookingMapper::toDto)
                            .toList();
            case PAST ->
                    bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).stream()
                            .map(bookingMapper::toDto)
                            .toList();
            case FUTURE ->
                    bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()).stream()
                            .map(bookingMapper::toDto)
                            .toList();
            case WAITING, REJECTED ->
                    bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(userId, BookingStatus.valueOf(state)).stream()
                            .map(bookingMapper::toDto)
                            .toList();
            case null -> throw new RuntimeException("Неизвестный статус бронирования: " + state);
        };
    }

    @Override
    public List<BookingDto> getAllByOwnerAndStatus(Long userId, String state) {
        userRepository.findById(userId)
                .orElseThrow(() -> new MyNotFoundException("Пользователь не найден"));
        return switch (BookingState.from(state).get()) {
            case ALL -> bookingRepository.findAllByItemOwnerIdOrderByStartDesc(userId).stream()
                    .map(bookingMapper::toDto)
                    .toList();
            case CURRENT ->
                    bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(userId, LocalDateTime.now(), LocalDateTime.now()).stream()
                            .map(bookingMapper::toDto)
                            .toList();
            case PAST ->
                    bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(userId, LocalDateTime.now()).stream()
                            .map(bookingMapper::toDto)
                            .toList();
            case FUTURE ->
                    bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(userId, LocalDateTime.now()).stream()
                            .map(bookingMapper::toDto)
                            .toList();
            case WAITING, REJECTED ->
                    bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(userId, BookingStatus.valueOf(state)).stream()
                            .map(bookingMapper::toDto)
                            .toList();
            case null -> throw new RuntimeException("Неизвестный статус бронирования: " + state);
        };
    }
}
