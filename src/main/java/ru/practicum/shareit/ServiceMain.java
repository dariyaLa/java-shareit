package ru.practicum.shareit;

import java.util.Collection;
import java.util.Optional;

public interface ServiceMain<T, K> {

    Optional<T> save(K entity);

    Optional<T> update(K entity, K newEntity);

    Optional<T> find(long id);

    Optional<T> find(String str);

    Collection<T> findAll();

    void delete(long id);
}
