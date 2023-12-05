package ru.practicum.shareIt.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping(path = "/bookings")
@PropertySource("classpath:application.properties")
@AllArgsConstructor
@Validated
public class BookingController {

    @Autowired
    private final BookingService bookingService;

    @PostMapping
    public BookingDtoOut create(@Valid @RequestBody BookingDto bookingDto, @RequestHeader(value = "${headers.userId}", required = true) Long userId) {

        log.debug("Получен POST запрос к эндпоинту: /bookings, Строка параметров запроса: '{}', iserId='{}'",
                bookingDto, userId);
        bookingDto.setBookerId(userId);
        bookingDto.setState(State.WAITING);
        return bookingService.save(BookingMapper.toEntity(bookingDto));
    }

    @PatchMapping("/{bookingId}")
    public BookingDtoOut update(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                @RequestParam(required = true) Boolean approved,
                                @PathVariable Long bookingId) {
        log.debug("Получен PATCH запрос к эндпоинту: /bookings, параметры запроса: userId='{}', " +
                "approved= '{}', bookingId= {} .", userId, approved, bookingId);
        return bookingService.update(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingDtoOut find(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                              @PathVariable Long bookingId) {
        return bookingService.find(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingDtoOut> findAll(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                             @RequestParam(required = false, defaultValue = "ALL") String state,
                                             @Min(0) @RequestParam(required = false, defaultValue = "0") int from,
                                             @Min(1) @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException exception) {
            State.exeptionState();
        }
        Pageable pageable = (Pageable) PageRequest.of(from / size, size, Sort.by("id").ascending());
        return bookingService.findAll(userId, State.valueOf(state), pageable);
    }

    @GetMapping("/owner")
    public Collection<BookingDtoOut> findAllOwner(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                                  @RequestParam(required = false, defaultValue = "ALL") String state,
                                                  @Min(0) @RequestParam(required = false, defaultValue = "0") int from,
                                                  @Min(1) @RequestParam(required = false, defaultValue = "1000") int size) {
        try {
            State.valueOf(state);
        } catch (IllegalArgumentException exception) {
            State.exeptionState();
        }
        Pageable pageable = (Pageable) PageRequest.of(from / size, size, Sort.by("id").descending());
        return bookingService.findAllOwner(userId, State.valueOf(state), pageable);
    }

}
