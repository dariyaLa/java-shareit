package ru.practicum.shareIt.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareIt.items.ItemDto;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private long id;
    @NotNull
    private String description;
    private Long requestor;
    private LocalDateTime created;
    List<ItemDto> items;
}
