package ru.practicum.shareIt.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@PropertySource("classpath:application.properties")
@AllArgsConstructor
@Validated
public class BookingControllerClient {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody BookingDto bookingDto, @RequestHeader(value = "${headers.userId}", required = true) Long userId) {

        log.debug("Получен POST запрос к эндпоинту: /bookings, Строка параметров запроса: '{}', iserId='{}'",
                bookingDto, userId);
        return bookingClient.create(bookingDto, userId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                         @RequestParam(required = true) Boolean approved,
                                         @PathVariable Long bookingId) {
        log.debug("Получен PATCH запрос к эндпоинту: /bookings, параметры запроса: userId='{}', " +
                "approved= '{}', bookingId= {} .", userId, approved, bookingId);
        return bookingClient.patch(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> find(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                       @PathVariable Long bookingId) {
        return bookingClient.getBooking(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                          @RequestParam(required = false, defaultValue = "ALL") String state,
                                          @Min(0) @RequestParam(required = false, defaultValue = "0") int from,
                                          @Min(1) @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException exception) {
            State.exeptionState(state);
        }
        return bookingClient.getBookings(userId, State.valueOf(state), from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findAllOwner(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                               @RequestParam(required = false, defaultValue = "ALL") String state,
                                               @Min(0) @RequestParam(required = false, defaultValue = "0") int from,
                                               @Min(1) @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException exception) {
            State.exeptionState(state);
        }
        return bookingClient.getBookingsForOwnersItems(userId, State.valueOf(state), from, size);
    }

}
