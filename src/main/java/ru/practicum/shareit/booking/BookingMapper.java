package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingDtoOut;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;

import java.util.Optional;

public final class BookingMapper {

    private BookingMapper() {}

    public static Optional<BookingDto> toDto(Booking booking) {
        if (booking == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(BookingDto.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .itemId(booking.getItemId())
                .bookerId(booking.getUserId())
                .state(booking.getState())
                .build());
    }

    public static Booking toEntity(BookingDto dto) {
        return Booking.builder()
                .id(dto.getId())
                .start(dto.getStart())
                .end(dto.getEnd())
                .itemId(dto.getItemId())
                .userId(dto.getBookerId())
                .state(dto.getState())
                .build();
    }

    public static BookingDtoOut toDto(Booking booking, ItemDto item, UserDto user) {
        return BookingDtoOut.builder()
                .id(booking.getId())
                .start(booking.getStart())
                .end(booking.getEnd())
                .item(ItemDto.builder().id(item.getId()).name(item.getName()).build())
                .booker(UserDto.builder().id(user.getId()).build())
                .status(booking.getState())
                .build();
    }

}
