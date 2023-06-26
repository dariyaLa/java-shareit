package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.ServiceMain;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ItemService implements ServiceMain<ItemDto, Item> {

    @Autowired
    ItemRepository itemRepository;
    @Autowired
    ItemMapper itemMapper;

    @Override
    public Optional<ItemDto> save(Item item) {
        return itemMapper.toDto(itemRepository.save(item));
    }

    @Override
    public Optional<ItemDto> update(Item item, Item newEntity) {
        return itemMapper.toDto(itemRepository.update(item, newEntity));
    }

    @Override
    public Optional<ItemDto> find(long id) {
        return itemMapper.toDto(itemRepository.find(id));
    }

    @Override
    public Optional<ItemDto> find(String str) {
        return Optional.empty();
    }

    @Override
    public Collection<ItemDto> findAll() {
        return null;
    }

    public Collection<ItemDto> findAll(long userId) {

        return itemRepository.findAll(userId).stream()
                .map(item -> itemMapper.toDto(item).get())
                .collect(Collectors.toList());
    }

    public Collection<ItemDto> findAll(long userId, String text) {

        return itemRepository.findAll(userId, text).stream()
                .map(item -> itemMapper.toDto(item).get())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {

    }
}
