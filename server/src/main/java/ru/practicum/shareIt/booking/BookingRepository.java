package ru.practicum.shareIt.booking;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from booking where item_id in " +
            "(select id from items where user_id = ?1)", nativeQuery = true)
    Page<Booking> findAllOwnerUserId(long userId, Pageable pageable); //выводим все брони ru.practicum.shareIt.gateway.users.items, владельцем которых являемся

    @Query(value = "select * from booking where start_date >= now() and item_id in " + //выводим все будущие брони ru.practicum.shareIt.gateway.users.items, владельцем которых являемся
            "(select id from items where user_id = ?1)", nativeQuery = true)
    Page<Booking> findAllOwnerUserIdInFuture(long userId, Pageable pageable);

    @Query(value = "select * from booking where end_date <= ?2 and item_id in " + //выводим все завершенные брони ru.practicum.shareIt.gateway.users.items, владельцем которых являемся
            "(select id from items where user_id = ?1)", nativeQuery = true)
    Page<Booking> findAllOwnerUserIdInPast(long userId, LocalDateTime now, Pageable pageable);

    @Query(value = "select * from booking where start_date <= ?2 and end_date > ?2 and item_id in " + //выводим все текущие брони ru.practicum.shareIt.gateway.users.items, владельцем которых являемся
            "(select id from items where user_id = ?1) ORDER BY start_date ASC limit 1", nativeQuery = true)
    List<Booking> findAllOwnerUserIdCurrent(long userId, LocalDateTime now);

    @Query(value = "select * from booking where item_id in " + //здесь
            "(select id from items where user_id = ?1 and id = ?2)", nativeQuery = true)
    List<Booking> findAllOwnerItemId(long userId, long id); //выводим все брони конкретной ru.practicum.shareIt.gateway.users.items, владельцем которой являемся

    Page<Booking> findAllByUserId(long userId, Pageable pageable);

    @Query(value = "select * from booking where id = ?1 and item_id in " +
            "(select id from items where user_id = ?2)", nativeQuery = true)
    Booking findByIdForOwner(long id, long userId);

    Booking findByIdAndUserId(long id, long userId);

    Page<Booking> findByUserIdAndState(long userId, State state, Pageable pageable);

    List<Booking> findAllByUserIdAndItemId(long userId, long itemId); //выводим все брони для user для одного item

    @Query(value = "select * from booking where state = ?2 and item_id in " + //для owner все его items с конкретным статусом
            "(select id from items where user_id = ?1)", nativeQuery = true)
    Page<Booking> findAllOwnerUserIdAndState(long userId, String state, Pageable pageable);

    Page<Booking> findByUserIdAndStartIsAfter(long userId, LocalDateTime now, Pageable pageable);

    Page<Booking> findByUserIdAndEndIsBefore(long userId, LocalDateTime now, Pageable pageable);

    @Query(value = "select * from booking where user_id = ?1 and end_date > ?2 " +
            "and start_date <= ?2", nativeQuery = true)
    Page<Booking> findAllByUserCurrentBooking(long userId, LocalDateTime now, Pageable pageable);

    @Query(value = "select * from booking where item_id = ?1 AND state ='APPROVED' " +
            "and start_date < ?2 ORDER BY end_date desc LIMIT 1", nativeQuery = true)
    Booking findLastBooking(long itemId, LocalDateTime now);

    @Query(value = "select * from booking where item_id = ?1 AND state ='APPROVED' " +
            "and start_date > ?2 ORDER BY start_date LIMIT 1", nativeQuery = true)
    Booking findNextBooking(long itemId, LocalDateTime now);

}
