package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@Builder
@AllArgsConstructor
public class ItemDto {

    private long id;
    @NotBlank
    private String name;
    @NotNull
    private String description;
    @NotNull(message = "Available not valid")
    private Boolean available;
    private Long owner;
    private ItemRequest request;
}
