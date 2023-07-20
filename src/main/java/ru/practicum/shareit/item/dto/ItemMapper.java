package ru.practicum.shareit.item.dto;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public final class ItemMapper {

    public static Optional<ItemDto> toDto(Item item) {
        if (item == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .requestId(item.getRequestId())
                .build());
    }

    public static Optional<ItemDto> toDtoWithBooking(Item item, List<BookingDto> bookingsOwner, Object comments) {
        if (item == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .requestId(item.getRequestId())
                .lastBooking(bookingsOwner.isEmpty() ? null : bookingsOwner.get(0))
                .nextBooking(bookingsOwner.size() < 2 ? null : bookingsOwner.get(1))
                .comments(comments == null ? "" : comments)
                .build());
    }


    public static Item toEntity(ItemDto dto) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .owner(dto.getOwner())
                .requestId(dto.getRequestId())
                .build();
    }

    public static Comments toEntity(CommentsDto commentsDto, long itemId, long userId) {
        return Comments.builder()
                .id(commentsDto.getId())
                .text(commentsDto.getText())
                .itemId(itemId)
                .author(userId)
                .created(LocalDateTime.now())
                .build();
    }

    public static CommentsDto toDto(Comments comments, User user) {
        return CommentsDto.builder()
                .id(comments.getId())
                .text(comments.getText())
                .authorName(user.getName())
                .created(LocalDateTime.now())
                .build();
    }

}
