package ru.practicum.shareit.user;

import java.util.Optional;

final class UserMapper {

    private static UserRepositoryImpl userRepository;

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
                .id(getId(dto))
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }


    private static long getId(UserDto dto) {
        if (dto.getId() == 0) {
            if (userRepository == null || userRepository.find(dto.getEmail()).isEmpty()) {
                return 0;
            }
            return userRepository.find(dto.getEmail()).get().getId();
        }
        return dto.getId();
    }


}
