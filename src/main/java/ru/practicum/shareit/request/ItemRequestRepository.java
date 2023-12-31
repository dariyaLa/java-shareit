package ru.practicum.shareit.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ItemRequestRepository extends JpaRepository<ItemRequest, Long> {

    Page<ItemRequest> findByRequestor(long userId, Pageable pageable);

    Page<ItemRequest> findByRequestorNot(long userId, Pageable pageable);

    ItemRequest findByIdAndRequestorNot(long id, long userId);
}
