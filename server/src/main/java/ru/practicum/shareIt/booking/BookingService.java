package ru.practicum.shareIt.booking;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.exception.ConflictDataBooking;
import ru.practicum.shareIt.exception.NotFoundData;
import ru.practicum.shareIt.exception.ValidationError;
import ru.practicum.shareIt.items.ItemDto;
import ru.practicum.shareIt.items.ItemService;
import ru.practicum.shareIt.users.UserDto;
import ru.practicum.shareIt.users.UserServiceImpl;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private ItemService itemService;
    @Autowired
    private UserServiceImpl userService;


    public BookingDtoOut save(Booking booking) {
        UserDto user = userService.find(booking.getUserId()).get();
        //проверяем, есть ли item
        ItemDto item = checkItem(booking.getItemId());
        //валидируем дату в booking
        validateBooking(booking, item);
        return BookingMapper.toDto(bookingRepository.save(booking), item, user);
    }

    public BookingDtoOut update(Long bookingId, Boolean state, long userId) {
        Booking booking = bookingRepository.findById(bookingId).get();
        if (State.valueOf("APPROVED").equals(booking.getState()) && state.equals(true)) {
            throw new ConflictDataBooking("Booking уже подтвержден");
        }
        //ищем item
        ItemDto item = itemService.find(booking.getItemId()).get();
        if (item.getOwner() != userId) {
            throw new NotFoundData("У userId отсутствуют права для approved");
        }
        booking.setState(State.getStateUser(state));
        UserDto user = userService.find(booking.getUserId()).get();
        return BookingMapper.toDto(bookingRepository.save(booking), item, user);
    }


    public BookingDtoOut find(long userId, long id) {
        Booking booking = bookingRepository.findByIdAndUserId(id, userId); //если автор бронирования
        Booking bookingsOwner = bookingRepository.findByIdForOwner(id, userId); //если owner item
        if (booking == null && bookingsOwner == null) {
            throw new NotFoundData("Not found data");
        }
        if (booking != null) {
            ItemDto item = itemService.find(booking.getItemId()).get();
            UserDto user = userService.find(userId).get();
            return BookingMapper.toDto(booking, item, user);
        } else {
            ItemDto item = itemService.find(bookingsOwner.getItemId()).get();
            UserDto user = userService.find(bookingsOwner.getUserId()).get();
            return BookingMapper.toDto(bookingsOwner, item, user);
        }
    }

    public Collection<BookingDtoOut> findAll(long userId, Pageable pageable) {
        Pageable pageableF = (Pageable) PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        UserDto user = userService.find(userId).get();
        return bookingRepository.findAllByUserId(userId, pageableF).stream()
                .map(i -> {
                    ItemDto itemDto = itemService.find(i.getItemId()).get();
                    return BookingMapper.toDto(i, itemDto, user);
                })
                .sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                .collect(Collectors.toList());
    }

    public Collection<BookingDtoOut> findAllOwner(long userId, State state, Pageable pageable) {
        UserDto user = userService.find(userId).get(); // здесь выкинется исключение, что пользователя с таким id нет
        LocalDateTime now = LocalDateTime.now();
        switch (state) {
            case ALL:
                return bookingRepository.findAllOwnerUserId(userId, pageable).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            UserDto userDto = userService.find(i.getUserId()).get();
                            return BookingMapper.toDto(i, itemDto, userDto);
                        }).sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            case FUTURE:
                return bookingRepository.findAllOwnerUserIdInFuture(userId, pageable).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            UserDto userDto = userService.find(i.getUserId()).get();
                            return BookingMapper.toDto(i, itemDto, userDto);
                        }).sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findAllOwnerUserIdAndState(userId, State.WAITING.name(), pageable).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            UserDto userDto = userService.find(i.getUserId()).get();
                            return BookingMapper.toDto(i, itemDto, userDto);
                        })
                        .sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findAllOwnerUserIdAndState(userId, State.REJECTED.name(), pageable).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            UserDto userDto = userService.find(i.getUserId()).get();
                            return BookingMapper.toDto(i, itemDto, userDto);
                        })
                        .sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findAllOwnerUserIdInPast(userId, now,pageable).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            UserDto userDto = userService.find(i.getUserId()).get();
                            return BookingMapper.toDto(i, itemDto, userDto);
                        }).sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllOwnerUserIdCurrent(userId, now).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            UserDto userDto = userService.find(i.getUserId()).get();
                            return BookingMapper.toDto(i, itemDto, userDto);
                        }).sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            default:
                State.exeptionState();
        }
        return null;
    }

    public Collection<BookingDtoOut> findAll(long userId, State state, Pageable pageable) {
        UserDto user = userService.find(userId).get();
        switch (state) {
            case ALL:
                return findAll(userId, pageable);
            case FUTURE:
                return bookingRepository.findByUserIdAndStartIsAfter(userId, LocalDateTime.now(), pageable).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            return BookingMapper.toDto(i, itemDto, user);
                        })
                        .sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            case WAITING:
                return bookingRepository.findByUserIdAndState(userId, State.WAITING, pageable).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            return BookingMapper.toDto(i, itemDto, user);
                        })
                        .sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            case REJECTED:
                return bookingRepository.findByUserIdAndState(userId, State.REJECTED, pageable).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            return BookingMapper.toDto(i, itemDto, user);
                        })
                        .sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            case PAST:
                return bookingRepository.findByUserIdAndEndIsBefore(userId, LocalDateTime.now(), pageable).stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            return BookingMapper.toDto(i, itemDto, user);
                        })
                        .sorted(Comparator.comparing(BookingDtoOut::getStart).reversed())
                        .collect(Collectors.toList());
            case CURRENT:
                return bookingRepository.findAllByUserCurrentBooking(userId, LocalDateTime.now() , pageable)
                        .stream()
                        .map(i -> {
                            ItemDto itemDto = itemService.find(i.getItemId()).get();
                            return BookingMapper.toDto(i, itemDto, user);
                        })
                        .sorted(Comparator.comparing(BookingDtoOut::getId))
                        .collect(Collectors.toList());
            default:
                State.exeptionState();
        }
        return null;
    }

    private void validateBooking(Booking booking, ItemDto itemDto) {
        if (booking.getEnd().isBefore(LocalDateTime.now())) {
            throw new ValidationError("Конец даты бронирования не может быть раньше текущей даты");
        }
        if (booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationError("Начало даты бронирования не может быть раньше текущей даты");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationError("Конец даты бронирования не может быть раньше даты начала");
        }
        if (booking.getEnd().isEqual(booking.getStart())) {
            throw new ValidationError("Конец даты бронирования не может равен дате начала");
        }
        //если владелец сам у себя запрашивает
        if (itemDto.getOwner() == booking.getUserId()) {
            throw new NotFoundData("Владелец вещи не может создать booking на item сам себе");
        }
    }

    private ItemDto checkItem(long id) {
        Optional<ItemDto> item = itemService.find(id);
        if (item.isPresent() && !item.get().getAvailable()) {
            throw new ConflictDataBooking("Item not available");
        }
        return item.get();
    }

}
