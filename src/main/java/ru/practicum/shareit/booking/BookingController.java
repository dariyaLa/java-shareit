package ru.practicum.shareit.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.user.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/bookings")
@PropertySource("classpath:application.properties")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut create(@Valid @RequestBody BookingDto bookingDto, @RequestHeader(value = "${headers.userId}", required = true) Long userId) {

        log.info("Получен POST запрос к эндпоинту: /bookings, Строка параметров запроса: '{}', iserId='{}'",
                bookingDto, userId);
        bookingDto.setBookerId(userId);
        bookingDto.setState(State.WAITING);
        return bookingService.save(BookingMapper.toEntity(bookingDto));
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut update(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                @RequestParam(required = true) Boolean approved,
                                @PathVariable Long bookingId) {
        log.info("Получен PATCH запрос к эндпоинту: /bookings, параметры запроса: userId='{}', " +
                "approved= '{}', bookingId= {} .", userId, approved, bookingId);
        return bookingService.update(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut find(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                              @PathVariable Long bookingId) {
        log.debug("Получен GET запрос к эндпоинту: /bookings, Строка параметров запроса: '{}'", bookingId);
        return bookingService.find(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDtoOut> findAll(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                             @RequestParam(required = false, defaultValue = "ALL") String state) {
        log.debug("Получен GET запрос к эндпоинту: /bookings, Строка параметров запроса: userId='{}'", userId);
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException exception) {
            State.exeptionState();
        }
        return bookingService.findAll(userId, State.valueOf(state));
    }

    @GetMapping("/owner")
    public Collection<BookingDtoOut> findAllOwner(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                                  @RequestParam(required = false, defaultValue = "ALL") String state) {
        log.debug("Получен GET запрос к эндпоинту: /bookings, Строка параметров запроса: userId='{}'", userId);
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException exception) {
            State.exeptionState();
        }
        return bookingService.findAllOwner(userId, State.valueOf(state));
    }

}
