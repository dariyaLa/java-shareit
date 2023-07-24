package ru.practicum.shareit.user;

import java.util.Optional;


public final class UserMapper {

    private UserMapper() {
    }

    public static Optional<UserDto> toDto(User user) {
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build());
    }

    public static User toEntity(UserDto dto) {
        return User.builder()
                .id(dto.getId())
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }
}
