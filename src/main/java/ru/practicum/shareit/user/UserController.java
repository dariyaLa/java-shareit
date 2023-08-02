package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.NotFoundData;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
//@Validated
public class UserController {

    private final UserServiceImpl userService;

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {
        log.info("Получен POST запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                userDto);
        return userService.save(UserMapper.toEntity(userDto)).get();

    }

    @GetMapping
    public Collection<UserDto> findAll() {
        log.debug("Получен GET запрос к эндпоинту: /users");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public UserDto find(@PathVariable int id) {
        log.debug("Получен GET запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                id);
        Optional<UserDto> userFindId = userService.find(id);
        if (userFindId.isEmpty()) {
            throw new NotFoundData("Not found data");
        }
        return userFindId.get();
    }

    @DeleteMapping("/{id}")
    public void deleteLike(@PathVariable int id) {
        log.info("Получен DELETE запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                id);
        userService.delete(id);
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto updateData, @PathVariable int id) {
        log.info("Получен PATCH запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                id);
        Optional<UserDto> userFindId = userService.find(id);
        if (userFindId.isEmpty()) {
            throw new NotFoundData("Not found data");
        }
        if (updateData.getName() != null) {
            userFindId.get().setName(updateData.getName());
        }
        if (updateData.getEmail() != null) {
            userFindId.get().setEmail(updateData.getEmail());
        }
        User userToEntity = UserMapper.toEntity(userFindId.get());
        return userService.update(userToEntity).get();
    }

}
