package ru.practicum.shareIt.users;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping(path = "/users")
@Validated
@RequiredArgsConstructor
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findUsers();
    }

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid UserDto userDto) {
        log.info("Получен POST запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                userDto);
        return userClient.createUser(userDto);

    }

    @PatchMapping("/{id}")
    public ResponseEntity<Object> update(@RequestBody UserDto updateData, @PathVariable long id) {
        log.info("Получен PATCH запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                id);
        return userClient.patch(id, updateData);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> find(@PathVariable int id) {
        log.debug("Получен GET запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                id);
        return userClient.findUserById(id);

    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        log.info("Получен DELETE запрос к эндпоинту: /users, Строка параметров запроса: '{}'",
                id);
        userClient.delete(id);
    }
}
