package ru.practicum.shareit.itemTest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comments;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;
    @Autowired
    private EntityManager em;

    private User user;
    private User userTwo;

    private Item item;

    private Booking lastBooking;
    private Booking nextBooking;

    @Test
    void saveItemTest() {
        Item newItem = Item.builder()
                .name("testNameNewItem")
                .description("testDescriptionNewItem")
                .available(true)
                .owner(user.getId())
                .build();
        itemService.save(newItem);

        TypedQuery<Item> query = em.createQuery("Select it from Item it where it.name = :name", Item.class);
        Item newItemSave = query.setParameter("name", newItem.getName()).getSingleResult();

        assertThat(newItem.getId(), notNullValue());
        assertThat(newItem.getName(), equalTo(newItemSave.getName()));
        assertThat(newItem.getDescription(), equalTo(newItemSave.getDescription()));

    }

    @Test
    void findItemTest() {
        ItemDto itemDtoFind = itemService.find(item.getId()).get();

        assertThat(itemDtoFind.getId(), notNullValue());
        assertThat(itemDtoFind.getName(), equalTo(item.getName()));
        assertThat(itemDtoFind.getDescription(), equalTo(item.getDescription()));

    }

    @Test
    void findAllItemTest() {
        Collection<ItemDto> itemDtoCollection = itemService.findAll();
        assertThat(itemDtoCollection.size(), equalTo(1));
    }

    @Test
    void findAllItemStringTest() {
        String text = "test";
        Collection<ItemDto> itemDtoCollection = itemService.findAll(text);
        assertThat(itemDtoCollection.size(), equalTo(1));
    }

    @Test
    void findWihtUserIdTest() {
        createBookings();
        ItemDto itemDtoFind = itemService.findWihtUserId(item.getId(), user.getId()).get();
        //проверяем, что для owner выдаются bookings
        assertThat(itemDtoFind.getId(), notNullValue());
        assertThat(itemDtoFind.getLastBooking(), equalTo(BookingMapper.toDto(lastBooking).get()));
        assertThat(itemDtoFind.getNextBooking(), equalTo(BookingMapper.toDto(nextBooking).get()));
    }

    @Test
    void findAllWihtUserIdTest() {
        createBookings();
        Collection<ItemDto> itemDtoCollection = itemService.findAllWithUser(user.getId());
        //проверяем, что для owner выдаются bookings
        assertThat(itemDtoCollection.size(), equalTo(1));
        assertTrue(itemDtoCollection.stream().allMatch(
                itemDto -> itemDto.getLastBooking().equals(BookingMapper.toDto(lastBooking).get())
                        && itemDto.getNextBooking().equals(BookingMapper.toDto(nextBooking).get())));
    }

    @Test
    void findWihtUserIdNoOwnerTest() {
        createBookings();
        ItemDto itemDtoFind = itemService.findWihtUserId(item.getId(), userTwo.getId()).get();
        //проверяем, что для noOwner не выдаются bookings
        assertThat(itemDtoFind.getId(), notNullValue());
        assertThat(itemDtoFind.getLastBooking(), equalTo(null));
        assertThat(itemDtoFind.getNextBooking(), equalTo(null));

    }

    @Test
    void updateItemTest() {
        item.setName("updateName");
        itemService.update(item);

        TypedQuery<Item> query = em.createQuery("Select it from Item it where it.name = :name", Item.class);
        Item newItemSave = query.setParameter("name", item.getName()).getSingleResult();

        assertThat(item.getId(), notNullValue());
        assertThat(item.getName(), equalTo("updateName"));
    }

    @Test
    void deleteItemTest() {
        itemService.delete(item.getId());

        assertThrows(NoResultException.class,
                () -> {
                    TypedQuery<Item> query = em.createQuery("Select it from Item it where it.name = :name", Item.class);
                    Item newItemSave = query.setParameter("name", item.getName()).getSingleResult();
                });
    }

    @Test
    void saveCommentTest() {
        createBookings();
        String commentText = "test comments";
        Comments comment = Comments.builder()
                .text(commentText)
                .itemId(item.getId())
                .author(userTwo.getId())
                .created(LocalDateTime.now())
                .build();
        itemService.saveComment(comment);

        TypedQuery<Comments> query = em.createQuery("Select com from Comments com where com.id = :id", Comments.class);
        Comments commentFind = query.setParameter("id", comment.getId()).getSingleResult();

        assertThat(commentFind.getId(), notNullValue());
        assertThat(commentFind.getText(), equalTo(commentText));
    }


    @BeforeEach
    private void init() {
        createUser();
        createItems();
    }

    public void createItems() {
        item = Item.builder()
                .id(3)
                .name("testName")
                .description("testDescription")
                .available(true)
                .owner(user.getId())
                .build();
        em.createNativeQuery("Insert into Items (id, name, description, available, user_id, request_id) " +
                        "values (:id,:name, :description, :available, :userId, :requestId)")
                .setParameter("id", item.getId())
                .setParameter("name", item.getName())
                .setParameter("description", item.getDescription())
                .setParameter("available", item.getAvailable())
                .setParameter("userId", item.getOwner())
                .setParameter("requestId", null)
                .executeUpdate();
    }

    public void createUser() {
        user = User.builder()
                .id(3)
                .name("testNameedde")
                .email("testdeded@email.com")
                .build();
        em.createNativeQuery("Insert into Users (id, name, email) values (:id,:name,:email)")
                .setParameter("id", user.getId())
                .setParameter("name", user.getName())
                .setParameter("email", user.getEmail())
                .executeUpdate();
        userTwo = User.builder()
                .id(4)
                .name("testNameTwo")
                .email("testUserTwo@email.com")
                .build();
        em.createNativeQuery("Insert into Users (id, name, email) values (:id,:name,:email)")
                .setParameter("id", userTwo.getId())
                .setParameter("name", userTwo.getName())
                .setParameter("email", userTwo.getEmail())
                .executeUpdate();
    }

    public void createBookings() {
        lastBooking = Booking.builder()
                .id(1)
                .start(LocalDateTime.now().minusMinutes(4))
                .end(LocalDateTime.now().minusMinutes(3))
                .itemId(item.getId())
                .userId(userTwo.getId())
                .state(State.APPROVED)
                .build();
        em.createNativeQuery("Insert into Booking (id, start_date, end_date,item_id,user_id,state) " +
                        "values (:id,:startDate,:endDate,:itemId,:userId,:state)")
                .setParameter("id", lastBooking.getId())
                .setParameter("startDate", lastBooking.getStart())
                .setParameter("endDate", lastBooking.getEnd())
                .setParameter("itemId", lastBooking.getItemId())
                .setParameter("userId", lastBooking.getUserId())
                .setParameter("state", lastBooking.getState().name())
                .executeUpdate();
        nextBooking = Booking.builder()
                .id(2)
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
                .itemId(item.getId())
                .userId(userTwo.getId())
                .state(State.APPROVED)
                .build();
        em.createNativeQuery("Insert into Booking (id, start_date, end_date,item_id,user_id,state) " +
                        "values (:id,:startDate,:endDate,:itemId,:userId,:state)")
                .setParameter("id", nextBooking.getId())
                .setParameter("startDate", nextBooking.getStart())
                .setParameter("endDate", nextBooking.getEnd())
                .setParameter("itemId", nextBooking.getItemId())
                .setParameter("userId", nextBooking.getUserId())
                .setParameter("state", nextBooking.getState().name())
                .executeUpdate();
    }

    @AfterEach
    public void clean() {
        em.createNativeQuery("delete from booking").executeUpdate();
        em.createNativeQuery("delete from items");
        em.createNativeQuery("delete from users");
    }

}
