package ru.practicum.shareit;

import java.util.Collection;
import java.util.Optional;

public interface ServiceMain<T, K> {

    Optional<T> save(K entity);

    Optional<T> update(K entity);

    Optional<T> find(long id);

    Collection<T> findAll();

    void delete(long id);
}
