package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.exeption.ConflictData;
import ru.practicum.shareit.exeption.NotFoundData;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserServiceImpl;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;

/**
 * TODO Sprint add-controllers.
 */
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
        itemDto.setOwner(userId);
        if (userId != null && userService.find(userId).isEmpty()) {
            throw new NotFoundData("Not found user");
        }
        if (itemService.find(itemDto.getId()).isEmpty()) {
            return itemService.save(ItemMapper.toEntity(itemDto)).get();
        } else {
            throw new ConflictData("Conflict data");
        }
    }

    @PatchMapping("/{itemId}")
    public ItemDto update(@RequestBody ItemDto updateData, @PathVariable int itemId, @RequestHeader(value = "${headers.userId}", required = true) Long userId) {
        log.info("Получен PATCH запрос к эндпоинту: /items, Строка параметров запроса: '{}'",
                itemId);
        //присвоили userId сущности, из которой будем брать данные для обновления
        updateData.setOwner(userId);
        if (userId != null && userService.find(userId).isEmpty()) {
            throw new NotFoundData("Not found user");
        }
        Optional<ItemDto> itemFindId = itemService.find(itemId);
        //ищем, есть ли user для обновления
        if (itemFindId.isEmpty()) {
            throw new NotFoundData("Not found data");
        }
        //проверка, что user совпадают
        if (itemFindId.get().getOwner() != null && !Objects.equals(itemFindId.get().getOwner(), userId)) {
            throw new NotFoundData("Not found item with userId=" + userId);
        }
        Item itemToEntity = ItemMapper.toEntity(itemFindId.get());
        Item itemNewDataToEntity = ItemMapper.toEntity(updateData);
        return itemService.update(itemToEntity, itemNewDataToEntity).get();
    }

    @GetMapping("/{id}")
    public ItemDto find(@PathVariable int id) {
        log.debug("Получен GET запрос к эндпоинту: /items, Строка параметров запроса: '{}'",
                id);
        Optional<ItemDto> itemFindId = itemService.find(id);
        if (itemFindId.isEmpty()) {
            throw new NotFoundData("Not found data");
        }
        return itemFindId.get();
    }

    @GetMapping
    public Collection<ItemDto> findAll(@RequestHeader(value = "${headers.userId}", required = true) Long userId) {
        log.debug("Получен GET запрос к эндпоинту: /items");
        return itemService.findAll(userId);
    }

    @GetMapping("/search")
    public Collection<ItemDto> find(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                    @RequestParam(required = false) String text) {
        log.debug("Получен GET запрос к эндпоинту: /items, Строка параметров запроса: '{}', " +
                "параметр запроса: '{}'.", userId, text);
        return itemService.findAll(userId, text);
    }

}

