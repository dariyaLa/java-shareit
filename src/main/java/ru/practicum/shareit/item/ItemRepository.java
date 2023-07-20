package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner(long userId);

    @Query(value = "select * " +
            "from items as it " +
            "where (it.name ILIKE %?1% or it.description ILIKE %?1%) " +
            "and it.available=true", nativeQuery = true)
    List<Item> findByNameContainingIgnoreCase(String search);

}
