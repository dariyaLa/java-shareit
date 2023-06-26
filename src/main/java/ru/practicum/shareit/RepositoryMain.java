package ru.practicum.shareit;

import java.util.Collection;
import java.util.Optional;

public interface RepositoryMain<T> {

    T save(T entity);

    T update(T entity, T updateData);

    T find(long id);

    Optional<T> find(String str);

    Collection<T> findAll();

    void delete(long id);


}
