package ru.practicum.shareIt.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDto {

    private long id;
    @NotNull
    @DateTimeFormat
    private LocalDateTime start;
    @NotNull
    private LocalDateTime end;
    @NotNull
    private long itemId;
    @NotNull
    private long bookerId;
    private State state;
}
