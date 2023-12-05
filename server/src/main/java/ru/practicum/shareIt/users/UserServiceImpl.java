package ru.practicum.shareIt.users;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareIt.ServiceMain;
import ru.practicum.shareIt.exception.NotFoundData;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements ServiceMain<UserDto, User> {

    @Autowired
    private UserRepository userRepository;


    @Override
    public Optional<UserDto> save(User user) {
        return UserMapper.toDto(userRepository.save(user));
    }

    @Override
    public Optional<UserDto> find(long id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) {
            throw new NotFoundData("Not found user");
        } else {
            return UserMapper.toDto(userRepository.findById(id).get());
        }
    }

    public Optional<User> find(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream()
                .map(user -> UserMapper.toDto(user).get())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<UserDto> update(User user) {
        return UserMapper.toDto(userRepository.save(user));
    }

}
