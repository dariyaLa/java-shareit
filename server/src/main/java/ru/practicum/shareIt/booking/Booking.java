package ru.practicum.shareIt.booking;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name = "start_date")
    @NotNull
    @DateTimeFormat
    private LocalDateTime start;
    @NotNull
    @Column(name = "end_date")
    private LocalDateTime end;
    @NotNull
    @Column(name = "item_id")
    private long itemId;
    @Column(name = "user_id")
    private long userId;
    @Enumerated(EnumType.STRING)
    @Column(name = "state")
    private State state;

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", start=" + start +
                ", end=" + end +
                ", itemId=" + itemId +
                ", userId=" + userId +
                ", state=" + state +
                '}';
    }
}
