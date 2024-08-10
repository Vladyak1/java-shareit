package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.List;

public interface BookingService {
    BookingDto saveBooking(Long userId, BookingDto bookingDto);

    BookingDto setApproved(Long userId, Long bookingId, Boolean approved);

    BookingDto getBooking(Long bookingId, Long userId);

    List<BookingDto> getAllByBookerAndStatus(Long userId, String state);

    List<BookingDto> getAllByOwnerAndStatus(Long userId, String state);
}
