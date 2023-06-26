package ru.practicum.shareit.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.ServiceMain;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements ServiceMain<UserDto, User> {

    @Autowired
    UserRepositoryImpl userRepository;
    @Autowired
    UserMapper userMapper;

    @Override
    public Optional<UserDto> save(User user) {
        return userMapper.toDto(userRepository.save(user));
    }

    @Override
    public Optional<UserDto> find(long id) {
        return userMapper.toDto(userRepository.find(id));
    }

    @Override
    public Optional<UserDto> find(String str) {
        Optional<User> user = userRepository.find(str);
        if (user.isEmpty()) {
            return Optional.empty();
        }
        return userMapper.toDto(user.get());
    }

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> userMapper.toDto(user).get())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        userRepository.delete(id);
    }

    @Override
    public Optional<UserDto> update(User user, User userNewData) {
        return userMapper.toDto(userRepository.update(user, userNewData));
    }
}
