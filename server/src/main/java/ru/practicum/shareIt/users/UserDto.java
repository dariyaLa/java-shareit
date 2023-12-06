package ru.practicum.shareIt.users;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

//@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {

    private long id;
    private String name;
    private String email;
}