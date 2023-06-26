package ru.practicum.shareit.item.dto;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.Optional;

@Service
public class ItemMapper {

    public Optional<ItemDto> toDto(Item item) {
        if (item == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .owner(item.getOwner())
                .request(item.getRequest() != null ? item.getRequest() : null)
                .build());
    }

    public Item toEntity(ItemDto dto) {
        return Item.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .owner(dto.getOwner())
                .request(dto.getRequest() != null ? dto.getRequest() : null)
                .build();
    }
}
