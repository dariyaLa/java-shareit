package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
class UserMapper {

    @Autowired
    private UserRepositoryImpl userRepository;

    public Optional<UserDto> toDto(User user) {
        if (user == null) {
            return Optional.empty();
        }
        return Optional.of(UserDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .build());
    }

    public User toEntity(UserDto dto) {
        return User.builder()
                .id(getId(dto)) //здесь 0, надо преобразовать
                .name(dto.getName())
                .email(dto.getEmail())
                .build();
    }


    private long getId(UserDto dto) {
        if (userRepository.find(dto.getEmail()).isEmpty()) {
            return 0;
        }
        return userRepository.find(dto.getEmail()).get().getId();
    }


}
