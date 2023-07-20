package ru.practicum.shareit.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-item-requests.
 */

@Entity
@Table(name = "requests")
@Data
@AllArgsConstructor
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Column(name = "description")
    private String description;
    @NotNull
    @Column(name = "user_id")
    private Long owner;
}
