package ru.practicum.shareIt.items;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findAllByOwner(long userId);

    @Query(value = "select * " +
            "from items as it " +
            "where (it.name ILIKE %?1% or it.description ILIKE %?1%) " +
            "and it.available=true", nativeQuery = true)
    List<Item> findByNameContainingIgnoreCase(String search);

    List<Item> findByRequestId(long id);

}
