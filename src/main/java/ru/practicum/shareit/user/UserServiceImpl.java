package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.ServiceMain;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserServiceImpl implements ServiceMain<UserDto, User> {

    UserRepositoryImpl userRepository;

    @Override
    public Optional<UserDto> save(User user) {
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public Optional<UserDto> find(long id) {
        return UserMapper.toDto(userRepository.findId(id));
    }

    @Override
    public Optional<UserDto> find(String str) {
        Optional<User> user = userRepository.find(str);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return UserMapper.toDto(user.get());
    }

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserMapper.toDto(user).get())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    @Override
    public Optional<UserDto> update(User user, User userNewData) {
        return UserMapper.toDto(userRepository.update(user, userNewData));
    }
}
