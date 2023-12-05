package ru.practicum.shareIt.requests;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareIt.items.ItemDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ItemRequestMapper {

    public static Optional<ItemRequestDto> toDto(ItemRequest itemRequest) {
        if (itemRequest == null) {
            return Optional.empty();
        }
        return Optional.of(ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .build());
    }

    public static Optional<ItemRequestDto> toDtoWithItems(ItemRequest itemRequest, List<ItemDto> itemsList) {
        if (itemRequest == null) {
            return Optional.empty();
        }
        return Optional.of(ItemRequestDto.builder()
                .id(itemRequest.getId())
                .description(itemRequest.getDescription())
                .requestor(itemRequest.getRequestor())
                .created(itemRequest.getCreated())
                .items(itemsList)
                .build());
    }

    public static ItemRequest toEntity(ItemRequestDto dto) {
        return ItemRequest.builder()
                .id(dto.getId())
                .description(dto.getDescription())
                .requestor(dto.getRequestor())
                .created(LocalDateTime.now())
                .build();
    }
}
