package ru.practicum.shareIt.requests;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareIt.exception.NotFoundData;
import ru.practicum.shareIt.users.UserServiceImpl;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.Collection;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@AllArgsConstructor
@PropertySource("classpath:application.properties")
@Validated
public class ItemRequestController {

    private ItemRequestServiceImpl requestService;
    private UserServiceImpl userService;

    @PostMapping
    public ItemRequestDto create(@RequestBody @Valid ItemRequestDto itemRequestDto,
                                 @RequestHeader(value = "${headers.userId}", required = true) Long userId) {
        log.info("Получен POST запрос к эндпоинту: /requests, Строка параметров запроса: '{}'",
                itemRequestDto);
        if (userService.find(userId).isEmpty()) {
            throw new NotFoundData("Not found user");
        }
        itemRequestDto.setRequestor(userId);
        return requestService.save(ItemRequestMapper.toEntity(itemRequestDto)).get();

    }

    @GetMapping
    public Collection<ItemRequestDto> find(@RequestHeader(value = "${headers.userId}", required = true) Long userId) {
        log.info("Получен GET запрос к эндпоинту: /requests, Строка параметров запроса: '{}'",
                userId);
        if (userService.find(userId).isEmpty()) {
            throw new NotFoundData("Not found user");
        }
        Pageable pageable = (Pageable) PageRequest.of(0, 1000);
        return requestService.findAll(userId, pageable);

    }

    @GetMapping("/{id}")
    public ItemRequestDto findByIid(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                    @PathVariable long id) {
        log.info("Получен GET запрос к эндпоинту: /requests, Строка параметров запроса: userId = '{}', id = '{}'",
                userId, id);
        if (userService.find(userId).isEmpty()) {
            throw new NotFoundData("Not found user");
        }
        return requestService.find(id).orElseThrow(() -> new NotFoundData("Not found data"));

    }

    @GetMapping("/all")
    public Collection<ItemRequestDto> findAll(@RequestHeader(value = "${headers.userId}", required = true) Long userId,
                                              @Min(0) @RequestParam(required = false, defaultValue = "1") int from,
                                              @Min(1) @RequestParam(required = false, defaultValue = "1") int size) {
        log.info("Получен GET запрос к эндпоинту: /requests, Строка параметров запроса: '{}'", userId);
        if (userService.find(userId).isEmpty()) {
            throw new NotFoundData("Not found user");
        }
        Pageable pageable = (Pageable) PageRequest.of(from, size);
        return requestService.findAllWithoutOwner(userId, pageable);

    }
}
