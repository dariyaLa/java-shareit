package ru.practicum.shareit.item;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.RepositoryMain;
import ru.practicum.shareit.item.model.Item;

import java.util.*;
import java.util.stream.Collectors;

@Repository
public class ItemRepository implements RepositoryMain<Item> {

    private Map<Long, Item> items = new HashMap<>();
    private long id = 0;

    @Override
    public Item save(Item item) {
        if (!items.containsValue(items)) {
            item.setId(getId());
            items.put(item.getId(), item);
            return item;
        }
        return null;
    }

    @Override
    public Item update(Item item, Item updateData) {
        if (items.containsKey(item.getId())) {
            Item itemUpdate = items.get(item.getId());
            if (updateData.getName() != null) {
                itemUpdate.setName(updateData.getName());
            }
            if (updateData.getDescription() != null) {
                itemUpdate.setDescription(updateData.getDescription());
            }
            if (updateData.getAvailable() != null) {
                itemUpdate.setAvailable(updateData.getAvailable());
            }
            if (updateData.getOwner() != null) {
                itemUpdate.setOwner(updateData.getOwner());
            }
            return itemUpdate;
        }
        return null;
    }

    @Override
    public Item find(long id) {
        return items.get(id);
    }

    @Override
    public Optional<Item> find(String str) {
        return Optional.empty();
    }

    @Override
    public Collection<Item> findAll() {
        return null;
    }

    public Collection<Item> findAll(long userId) {
        List<Item> itemList = new ArrayList<Item>(items.values());
        return itemList.stream()
                .filter(i -> i.getOwner() != null && i.getOwner() == userId)
                .collect(Collectors.toList());
    }

    public Collection<Item> findAll(long userId, String text) {
        List<Item> itemList = new ArrayList<Item>(items.values());
        if (text == null || text.equals("")) {
            return new ArrayList<Item>();
        }
        return itemList.stream()
                .filter(i -> i.getName().toLowerCase().contains(text.toLowerCase())
                        || i.getDescription().toLowerCase().contains(text.toLowerCase())
                        && i.getAvailable())
                .collect(Collectors.toList());
    }

    @Override
    public void delete(long id) {

    }

    private long getId() {
        return ++id;
    }
}
