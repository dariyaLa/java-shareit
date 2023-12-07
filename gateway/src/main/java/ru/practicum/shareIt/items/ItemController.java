package ru.practicum.shareIt.items;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
@PropertySource("classpath:application.properties")
public class ItemController {

    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> create(@RequestBody @Valid ItemDto itemDto, @RequestHeader(value = "${headers.userId}", required = true) Long userId) {

        log.info("Получен POST запрос к эндпоинту: /items, Строка параметров запроса: '{}'",
                itemDto);
        itemDto.setOwner(userId);
        return itemClient.create(itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> update(@RequestBody ItemDto updateData, @PathVariable Long itemId, @RequestHeader(value = "${headers.userId}", required = true) Long userId) {
        log.info("Получен PATCH запрос к эндпоинту: /users.items, Строка параметров запроса: '{}'",
                itemId);
        return itemClient.patch(updateData, itemId, userId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> find(@PathVariable int id, @RequestHeader(value = "${headers.userId}", required = false) Long userId) {
        log.debug("Получен GET запрос к эндпоинту: /items, Строка параметров запроса: '{}'", id);
        return itemClient.get(id, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findAll(@RequestHeader(value = "${headers.userId}", required = true) Long userId) {
        log.debug("Получен GET запрос к эндпоинту: /items");
        return itemClient.get(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> find(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                       @RequestParam(required = false) String text) {
        log.info("Получен GET запрос к эндпоинту: /items, Строка параметров запроса: '{}', " +
                "параметр запроса: '{}'.", userId, text);
        return itemClient.get(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestBody @Valid CommentsDto commentsDto, @RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                                @PathVariable int itemId) {

        log.info("Получен POST запрос к эндпоинту: /comment, Строка параметров запроса: userId = '{}' , itemId = '{}'", userId, itemId);
        return itemClient.addComment(userId, itemId, commentsDto);
    }

}

