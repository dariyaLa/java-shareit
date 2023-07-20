package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "items")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank
    @Column(name = "name")
    private String name;
    @NotNull
    @Column(name = "description")
    private String description;
    @NotNull(message = "Available not valid")
    @Column(name = "available")
    private Boolean available;
    @Column(name = "user_id")
    private Long owner;
    @Column(name = "request_id")
    private Long requestId;

}
