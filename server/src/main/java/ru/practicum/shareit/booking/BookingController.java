package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingRequestDto;

import java.util.Collection;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingDto> create(@RequestHeader("X-Sharer-User-Id") Long userId,
                                             @RequestBody BookingRequestDto bookingRequestDto) {
        log.info("Вызов метода POST бронирования: userId={}, booking={}", userId, bookingRequestDto);
        return ResponseEntity.ok().body(bookingService.saveBooking(userId, bookingRequestDto));
    }

    @GetMapping
    public ResponseEntity<Collection<BookingDto>> findAll(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Вызов метода GET всех бронирований для пользователя с id={}", userId);
        return ResponseEntity.ok().body(bookingService.getAllByBookerAndStatus(userId, state));
    }

    @GetMapping("/owner")
    public ResponseEntity<Collection<BookingDto>> findAllByOwnerAndStatus(
            @RequestHeader("X-Sharer-User-Id") Long userId,
            @RequestParam(defaultValue = "ALL", required = false) String state) {
        log.info("Вызов метода GET всех бронирований для пользователя с id={} и статусом={}", userId, state);
        return ResponseEntity.ok().body(bookingService.getAllByOwnerAndStatus(userId, state));
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<BookingDto> setApproved(@RequestHeader("X-Sharer-User-Id") Long userId,
                                  @PathVariable Long bookingId,
                                  @RequestParam Boolean approved) {
        log.info("Вызов метода PATCH для подтверждения бронирования с id={}", bookingId);
        return ResponseEntity.ok().body(bookingService.setApproved(userId, bookingId, approved));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingDto> findById(@RequestHeader("X-Sharer-User-Id") Long userId,
                               @PathVariable Long bookingId) {
        log.info("Вызов метода GET бронирования с id={}", bookingId);
        return ResponseEntity.ok().body(bookingService.getBooking(bookingId, userId));
    }
}