package ru.practicum.shareit.item.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ItemDto {

    private long id;
    @NotBlank
    private String name;
    @NotNull
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String description;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @NotNull(message = "Available not valid")
    private Boolean available;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long owner;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private Object comments;
}
