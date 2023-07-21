package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    @Query(value = "select * from booking where item_id in " +
            "(select id from items where user_id = ?1)", nativeQuery = true)
    List<Booking> findAllOwnerUserId(long userId); //выводим все брони items, владельцем которых являемся

    @Query(value = "select * from booking where start_date >= now() and item_id in " + //выводим все будущие брони items, владельцем которых являемся
            "(select id from items where user_id = ?1)", nativeQuery = true)
    List<Booking> findAllOwnerUserIdInFuture(long userId);

    @Query(value = "select * from booking where end_date <= now() and item_id in " + //выводим все завершенные брони items, владельцем которых являемся
            "(select id from items where user_id = ?1)", nativeQuery = true)
    List<Booking> findAllOwnerUserIdInPast(long userId);

    @Query(value = "select * from booking where end_date > now() and date_trunc('day', start_date) = date_trunc('day', now()) and item_id in " + //выводим все текущие брони items, владельцем которых являемся
            "(select id from items where user_id = ?1) ORDER BY start_date ASC limit 1", nativeQuery = true)
    List<Booking> findAllOwnerUserIdCurrent(long userId);

    @Query(value = "select * from booking where item_id in " + //здесь
            "(select id from items where user_id = ?1 and id = ?2)", nativeQuery = true)
    List<Booking> findAllOwnerItemId(long userId, long id); //выводим все брони конкретной items, владельцем которой являемся

    List<Booking> findAllByUserId(long userId);

    @Query(value = "select * from booking where id = ?1 and item_id in " +
            "(select id from items where user_id = ?2)", nativeQuery = true)
    Booking findByIdForOwner(long id, long userId);

    Booking findByIdAndUserId(long id, long userId);

    List<Booking> findByUserIdAndState(long userId, State state);

    List<Booking> findAllByUserIdAndItemId(long userId, long itemId); //выводим все брони для user для одного item

    @Query(value = "select * from booking where state = ?2 and item_id in " + //для owner все его items с конкретным статусом
            "(select id from items where user_id = ?1)", nativeQuery = true)
    List<Booking> findAllOwnerUserIdAndState(long userId, String state);

    @Query(value = "select * from booking bo " +
            "where bo.user_id = ?1 and bo.start_date >= now()", nativeQuery = true)
    List<Booking> findAllByUserFutureBooking(long userId, LocalDateTime dateTime);

    @Query(value = "select * from booking bo " +
            "where bo.user_id = ?1 and bo.end_date <= now()", nativeQuery = true)
    List<Booking> findAllByUserPastBooking(long userId, LocalDateTime dateTime);

    @Query(value = "select * from booking where user_id = ?1 and end_date > now() " +
            "and date_trunc('day', start_date) = date_trunc('day', now()) ORDER BY start_date ASC", nativeQuery = true)
    List<Booking> findAllByUserCurrentBooking(long userId, LocalDateTime dateTime);

    @Query(value = "select * from booking where item_id = ?1 AND state ='APPROVED' " +
            "and start_date < now() ORDER BY end_date DESC LIMIT 1", nativeQuery = true)
    Booking findLastBooking(long itemId);

    @Query(value = "select * from booking where item_id = ?1 AND state ='APPROVED' " +
            "and start_date > now() ORDER BY start_date ASC LIMIT 1", nativeQuery = true)
    Booking findNextBooking(long itemId);

}
