package ru.practicum.shareit.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ConflictData;
import ru.practicum.shareit.exeption.NotFoundData;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
@Slf4j
@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserServiceImpl userService;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserServiceImpl userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @PostMapping
    public UserDto create(@RequestBody @Valid UserDto userDto) {

        log.info("Получен POST запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                userDto);
        if (userService.find(userDto.getEmail()).isEmpty()) {
            return userService.save(userMapper.toEntity(userDto)).get();
        } else {
            throw new ConflictData("Conflict data");
        }
    }

    @PatchMapping("/{id}")
    public UserDto update(@RequestBody UserDto updateData, @PathVariable int id) {
        log.info("Получен PATCH запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                id);
        Optional<UserDto> userFindId = userService.find(id);
        //ищем, есть ли user для обновления
        if (userFindId.isEmpty()) {
            throw new NotFoundData("Not found data");
        }
        // ищем, может быть email уже зарегистрирован на другом user
        if (updateData.getEmail() != null) {
            Optional<UserDto> userFindEmail = userService.find(updateData.getEmail());
            if (userFindEmail.isPresent() && userFindEmail.get().getId() != id) {
                throw new ConflictData("Conflict data");
            }
        }
        /*Optional<UserDto> user = userService.update(userMapper.toEntity(userService.find(id).get()),
                userMapper.toEntity(updateData));
        return user.get();*/
        User userToEntity = userMapper.toEntity(userFindId.get());
        User userNewDataToEntity = userMapper.toEntity(updateData);
        return userService.update(userToEntity, userNewDataToEntity).get();
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

    @GetMapping
    public Collection<UserDto> findAll() {
        log.debug("Получен GET запрос к эндпоинту: /users");
        return userService.findAll();
    }

    @DeleteMapping("/{id}")
    public void deleteLike(@PathVariable int id) {
        log.info("Получен DELETE запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                id);
        userService.delete(id);
    }
}
