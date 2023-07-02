package ru.practicum.shareit.item;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.ServiceMain;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ItemService implements ServiceMain<ItemDto, Item> {

    ItemRepository itemRepository;

    @Override
    public Optional<ItemDto> save(Item item) {
        return ItemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public Optional<ItemDto> update(Item item, Item newEntity) {
        return ItemMapper.toDto(itemRepository.update(item, newEntity));
    }

    @Override
    public Optional<ItemDto> find(long id) {
        return ItemMapper.toDto(itemRepository.findId(id));
    }

    @Override
    public Optional<ItemDto> find(String str) {
        return Optional.empty();
    }

    @Override
    public Collection<ItemDto> findAll() {
        return itemRepository.findAll().stream()
                .map(item -> ItemMapper.toDto(item).get())
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> findAll(long userId) {

        return itemRepository.findAll(userId).stream()
                .map(item -> ItemMapper.toDto(item).get())
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> findAll(long userId, String text) {

        return itemRepository.findAll(userId, text).stream()
                .map(item -> ItemMapper.toDto(item).get())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {
        itemRepository.delete(id);
    }
}
