package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.exeption.ValidationError;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class BookingServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private BookingService bookingService;

    private Item item;

    private User user;

    private User userOwner;

    private Booking booking = new Booking(3, LocalDateTime.now().plusMinutes(1), LocalDateTime.now().plusMinutes(2),
            4, 4, State.WAITING);

    @Test
    void saveBookingTest() {
        deleteBooking();
        Booking newBooking = Booking.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
                .itemId(item.getId())
                .userId(user.getId())
                .state(State.WAITING)
                .build();
        bookingService.save(newBooking);

        TypedQuery<Booking> query = em.createQuery("Select bo from Booking bo where bo.userId = :userId", Booking.class);
        Booking newBookingSave = query.setParameter("userId", newBooking.getUserId()).getSingleResult();

        assertThat(newBookingSave.getId(), notNullValue());
        assertThat(newBookingSave.getStart(), equalTo(newBooking.getStart()));
        assertThat(newBookingSave.getEnd(), equalTo(newBooking.getEnd()));
        assertThat(newBookingSave.getItemId(), equalTo(newBooking.getItemId()));
    }

    @Test
    void updateBookingTest() {
        createBookings(booking);
        bookingService.update(booking.getId(), true, userOwner.getId());

        TypedQuery<Booking> query = em.createQuery("Select bo from Booking bo where bo.userId = :userId", Booking.class);
        Booking newBookingSave = query.setParameter("userId", booking.getUserId()).getSingleResult();

        assertThat(newBookingSave.getId(), notNullValue());
        assertThat(newBookingSave.getState(), equalTo(State.APPROVED));
    }

    @Test
    void findAllBookingTest() {
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAll(user.getId(), State.ALL, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllOwnerBookingTest() {
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAllOwner(userOwner.getId(), State.ALL, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllFutureBookingTest() {
        booking.setStart(LocalDateTime.now().plusMinutes(20));
        booking.setEnd(LocalDateTime.now().plusMinutes(30));
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAll(user.getId(), State.FUTURE, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllOwnerFutureBookingTest() {
        booking.setStart(LocalDateTime.now().plusMinutes(20));
        booking.setEnd(LocalDateTime.now().plusMinutes(30));
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAllOwner(userOwner.getId(), State.FUTURE, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllPastBookingTest() {
        booking.setStart(LocalDateTime.now().minusMinutes(30));
        booking.setEnd(LocalDateTime.now().minusMinutes(20));
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAll(user.getId(), State.PAST, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllOwnerPastBookingTest() {
        booking.setStart(LocalDateTime.now().minusMinutes(30));
        booking.setEnd(LocalDateTime.now().minusMinutes(20));
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAllOwner(userOwner.getId(), State.PAST, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllWaitingBookingTest() {
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAll(user.getId(), State.WAITING, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllOwnerWaitingBookingTest() {
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAllOwner(userOwner.getId(), State.WAITING, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllRejectedBookingTest() {
        booking.setState(State.REJECTED);
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAll(user.getId(), State.REJECTED, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllOwnerRejectedBookingTest() {
        booking.setState(State.REJECTED);
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAllOwner(userOwner.getId(), State.REJECTED, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllCurrentBookingTest() {
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAll(user.getId(), State.CURRENT, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findAllOwnerCurrentBookingTest() {
        createBookings(booking);
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<BookingDtoOut> bookings = bookingService.findAllOwner(userOwner.getId(), State.CURRENT, pageable);
        assertThat(bookings.size(), is(1));
    }

    @Test
    void findBookingTest() {
        createBookings(booking);
        BookingDtoOut bookingFind = bookingService.find(user.getId(), booking.getId());
        assertThat(bookingFind.getId(), notNullValue());
    }

    @Test
    void findOwnerBookingTest() {
        createBookings(booking);
        BookingDtoOut bookingFind = bookingService.find(userOwner.getId(), booking.getId());
        assertThat(bookingFind.getId(), notNullValue());
    }

    @Test
    void expEndDateTest() {
        Throwable thrown = assertThrows(ValidationError.class,
                () -> {
                    booking.setEnd(LocalDateTime.now().minusDays(1));
                    bookingService.save(booking);
                });

        assertEquals("Конец даты бронирования не может быть раньше текущей даты", thrown.getMessage());
    }

    @Test
    void expStartdDteTest() {
        Throwable thrown = assertThrows(ValidationError.class,
                () -> {
                    booking.setStart(LocalDateTime.now().minusDays(1));
                    bookingService.save(booking);
                });

        assertEquals("Начало даты бронирования не может быть раньше текущей даты", thrown.getMessage());
    }

    @BeforeEach
    private void init() {
        createUser();
        createItems();
    }

    @AfterEach
    public void clean() {
        deleteBooking();
        em.createNativeQuery("delete from items");
        em.createNativeQuery("delete from users");
    }

    public void deleteBooking() {
        em.createNativeQuery("delete from booking").executeUpdate();
    }

    public void createUser() {
        user = User.builder()
                .id(4)
                .name("ownerFoBooking")
                .email("ownerBooking@email.com")
                .build();
        em.createNativeQuery("Insert into Users (id, name, email) values (:id,:name,:email)")
                .setParameter("id", user.getId())
                .setParameter("name", user.getName())
                .setParameter("email", user.getEmail())
                .executeUpdate();
        userOwner = User.builder()
                .id(5)
                .name("testNameFoBooking")
                .email("testFoBooking@email.com")
                .build();
        em.createNativeQuery("Insert into Users (id, name, email) values (:id,:name,:email)")
                .setParameter("id", userOwner.getId())
                .setParameter("name", userOwner.getName())
                .setParameter("email", userOwner.getEmail())
                .executeUpdate();
    }

    public void createItems() {
        item = Item.builder()
                .id(4)
                .name("testNameFoBooking")
                .description("testDescriptionFoBooking")
                .available(true)
                .owner(userOwner.getId())
                .build();
        em.createNativeQuery("Insert into Items (id, name, description, available, user_id, request_id) " +
                        "values (:id,:name, :description, :available, :userId, :requestId)")
                .setParameter("id", item.getId())
                .setParameter("name", item.getName())
                .setParameter("description", item.getDescription())
                .setParameter("available", item.getAvailable())
                .setParameter("userId", item.getOwner())
                .setParameter("requestId", null)
                .executeUpdate();
    }

    public void createBookings(Booking booking) {
        em.createNativeQuery("Insert into Booking (id, start_date, end_date,item_id,user_id,state) " +
                        "values (:id,:startDate,:endDate,:itemId,:userId,:state)")
                .setParameter("id", booking.getId())
                .setParameter("startDate", booking.getStart())
                .setParameter("endDate", booking.getEnd())
                .setParameter("itemId", booking.getItemId())
                .setParameter("userId", booking.getUserId())
                .setParameter("state", booking.getState().name())
                .executeUpdate();
    }
}
