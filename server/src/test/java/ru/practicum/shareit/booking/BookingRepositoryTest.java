package ru.practicum.shareit.booking;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    private final User booker = User.builder()
            .name("name")
            .email("email@email.com")
            .build();

    private final User owner = User.builder()
            .name("name2")
            .email("email2@email.com")
            .build();

    private final Item item = Item.builder()
            .name("name")
            .description("description")
            .available(true)
            .owner(owner)
            .build();

    private final Booking booking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().minusHours(1L))
            .end(LocalDateTime.now().plusDays(1L))
            .build();

    private final Booking pastBooking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().minusDays(2L))
            .end(LocalDateTime.now().minusDays(1L))
            .build();

    private final Booking futureBooking = Booking.builder()
            .item(item)
            .booker(booker)
            .status(BookingStatus.APPROVED)
            .start(LocalDateTime.now().plusDays(1L))
            .end(LocalDateTime.now().plusDays(2L))
            .build();


    @BeforeEach
    public void init() {
        testEntityManager.persist(booker);
        testEntityManager.persist(owner);
        testEntityManager.persist(item);
        testEntityManager.flush();
        bookingRepository.save(booking);
        bookingRepository.save(pastBooking);
        bookingRepository.save(futureBooking);
    }

    @AfterEach
    public void deleteAll() {
        bookingRepository.deleteAll();
    }

    @Test
    void findAllByBookerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository.findAllByBookerIdOrderByStartDesc(booker.getId());
        assertEquals(bookings.size(), 3);
        assertEquals(bookings.getFirst().getBooker().getId(), 1L);
    }

    @Test
    void findAllByBookerIdAndEndBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.getFirst().getBooker().getId(), 1L);
    }

    @Test
    void findAllByBookerIdAndStartAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndEndBeforeOrderByStartDesc(booker.getId(), LocalDateTime.now());
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.getFirst().getBooker().getId(), 1L);
    }

    @Test
    void findAllByBookerIdAndStatusOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(), BookingStatus.APPROVED);
        assertEquals(bookings.size(), 3);
        assertEquals(bookings.getFirst().getBooker().getId(), 1L);
    }

    @Test
    void findAllByItemOwnerIdOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByBookerIdAndStatusOrderByStartDesc(booker.getId(), BookingStatus.APPROVED);
        assertEquals(bookings.size(), 3);
        assertEquals(bookings.getFirst().getItem().getOwner().getId(), 2L);
    }

    @Test
    void findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(
                        owner.getId(), LocalDateTime.now(), LocalDateTime.now()
                );
        assertEquals(bookings.size(), 1);
        assertEquals(bookings.getFirst().getItem().getOwner().getId(), 2L);
    }

    @Test
    void findAllByItemOwnerIdAndEndBeforeOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(
                        owner.getId(), LocalDateTime.now().plusDays(3)
                );
        assertEquals(bookings.size(), 3);
        assertEquals(bookings.getFirst().getItem().getOwner().getId(), 2L);
    }

    @Test
    void findAllByItemOwnerIdAndStartAfterOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdAndStartAfterOrderByStartDesc(
                        owner.getId(), LocalDateTime.now().minusDays(4)
                );
        assertEquals(bookings.size(), 3);
        assertEquals(bookings.getFirst().getItem().getOwner().getId(), 2L);
    }

    @Test
    void findAllByItemOwnerIdAndStatusOrderByStartDesc() {
        List<Booking> bookings = bookingRepository
                .findAllByItemOwnerIdAndStatusOrderByStartDesc(
                        owner.getId(), BookingStatus.APPROVED
                );
        assertEquals(bookings.size(), 3);
        assertEquals(bookings.getFirst().getId(), 3L);
    }
}