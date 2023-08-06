package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.NotFoundData;
import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserServiceImpl;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/items")
@AllArgsConstructor
@PropertySource("classpath:application.properties")
public class ItemController {

    private final ItemService itemService;
    private final UserServiceImpl userService;

    @PostMapping
    public ItemDto create(@RequestBody @Valid ItemDto itemDto, @RequestHeader(value = "${headers.userId}", required = true) Long userId) {

        log.info("Получен POST запрос к эндпоинту: /items, Строка параметров запроса: '{}'",
                itemDto);
        Optional<UserDto> user = userService.find(userId);
        if (userService.find(userId).isEmpty()) {
            throw new NotFoundData("Not found user");
        }
        itemDto.setOwner(userId);
        return itemService.save(ItemMapper.toEntity(itemDto)).get();
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto updateData, @PathVariable int itemId, @RequestHeader(value = "${headers.userId}", required = true) Long userId) {
        log.info("Получен PATCH запрос к эндпоинту: /items, Строка параметров запроса: '{}'",
                itemId);
        Optional<ItemDto> itemFindId = itemService.find(itemId);
        //ищем, есть ли user для обновления
        if (itemFindId.isEmpty()) {
            throw new NotFoundData("Not found user");
        }
        //проверка, что user совпадают
        if (itemFindId.get().getOwner() != null && !Objects.equals(itemFindId.get().getOwner(), userId)) {
            throw new NotFoundData("Not found item with userId=" + userId);
        }
        //собираем данные для обновления
        if (updateData.getName() != null) {
            itemFindId.get().setName(updateData.getName());
        }
        if (updateData.getDescription() != null) {
            itemFindId.get().setDescription(updateData.getDescription());
        }
        if (updateData.getAvailable() != null) {
            itemFindId.get().setAvailable(updateData.getAvailable());
        }
        Item itemNewDataToEntity = ItemMapper.toEntity(itemFindId.get());
        return itemService.save(itemNewDataToEntity).get();
    }

    @GetMapping("/{id}")
    public ItemDto find(@PathVariable int id, @RequestHeader(value = "${headers.userId}", required = false) Long userId) {
        log.debug("Получен GET запрос к эндпоинту: /items, Строка параметров запроса: '{}'", id);
        Optional<ItemDto> itemFindId = itemService.findWihtUserId(id, userId);

        if (itemFindId.isEmpty()) {
            throw new NotFoundData("Not found data");
        }
        return itemFindId.get();
    }

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader(value = "${headers.userId}", required = true) Long userId) {
        log.debug("Получен GET запрос к эндпоинту: /items");
        return itemService.findAllWithUser(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> find(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                    @RequestParam(required = false) String text) {
        log.info("Получен GET запрос к эндпоинту: /items, Строка параметров запроса: '{}', " +
                "параметр запроса: '{}'.", userId, text);
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemService.findAll(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentsDto createComment(@RequestBody @Valid CommentsDto commentsDto, @RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                     @PathVariable int itemId) {

        log.info("Получен POST запрос к эндпоинту: /comment, Строка параметров запроса: userId = '{}' , itemId = '{}'", userId, itemId);
        return itemService.saveComment(ItemMapper.toEntity(commentsDto, itemId, userId));
    }

}

