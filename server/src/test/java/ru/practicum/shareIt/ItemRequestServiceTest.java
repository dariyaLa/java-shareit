package ru.practicum.shareIt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareIt.exception.NotFoundData;
import ru.practicum.shareIt.requests.ItemRequest;
import ru.practicum.shareIt.requests.ItemRequestDto;
import ru.practicum.shareIt.requests.ItemRequestServiceImpl;
import ru.practicum.shareIt.users.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Collection;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@Transactional
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class ItemRequestServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private ItemRequestServiceImpl itemRequestService;

    private User userOwner;

    private User user;

    private ItemRequest itemRequest;

    @Test
    void saveItemReqTest() {
        cleanRequests();
        ItemRequest newItemReq = ItemRequest.builder()
                .description("testDescriptionNewItemReq")
                .requestor(userOwner.getId())
                .created(LocalDateTime.now())
                .build();
        itemRequestService.save(newItemReq);

        TypedQuery<ItemRequest> query = em.createQuery("Select it from ItemRequest it where it.requestor = :userId", ItemRequest.class);
        ItemRequest newItemReqSave = query.setParameter("userId", newItemReq.getRequestor()).getSingleResult();

        assertThat(newItemReqSave.getId(), notNullValue());
        assertThat(newItemReqSave.getDescription(), equalTo(newItemReq.getDescription()));

    }

    @Test
    void updateItemReqTest() {
        String descriptionUpdate = "updateDescription";
        itemRequest.setDescription(descriptionUpdate);
        itemRequestService.update(itemRequest);

        TypedQuery<ItemRequest> query = em.createQuery("Select it from ItemRequest it where it.id = :id", ItemRequest.class);
        ItemRequest itemReqFind = query.setParameter("id", itemRequest.getId()).getSingleResult();

        assertThat(itemReqFind.getId(), notNullValue());
        assertThat(itemReqFind.getDescription(), equalTo(descriptionUpdate));

    }

    @Test
    void findItemReqTest() {
        ItemRequestDto itemDtoFind = itemRequestService.find(itemRequest.getId()).get();

        assertThat(itemDtoFind.getId(), notNullValue());
        assertThat(itemDtoFind.getDescription(), equalTo(itemRequest.getDescription()));

    }

    @Test
    void findAllOwnerItemReqTest() {
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<ItemRequestDto> itemRequestDtoCollection = itemRequestService.findAll(userOwner.getId(), pageable);

        assertThat(itemRequestDtoCollection.size(), equalTo(1));
    }

    @Test
    void findAllItemReqTest() {
        Pageable pageable = (Pageable) PageRequest.of(0, 2, Sort.by("id").descending());
        Collection<ItemRequestDto> itemRequestDtoCollection = itemRequestService.findAllWithoutOwner(user.getId(), pageable);

        assertThat(itemRequestDtoCollection.size(), equalTo(1));
    }

    @Test
    void findAllWithoutUserItemReqTest() {
        Collection<ItemRequestDto> itemRequestDtoCollection = itemRequestService.findAll();
        assertThat(itemRequestDtoCollection.size(), equalTo(1));
    }

    @Test
    void deleteItemReqTest() {
        itemRequestService.delete(itemRequest.getId());
        assertThrows(NoResultException.class,
                () -> {
                    TypedQuery<ItemRequest> query = em.createQuery("Select it from ItemRequest it where it.id = :id", ItemRequest.class);
                    ItemRequest itemReqFind = query.setParameter("id", itemRequest.getId()).getSingleResult();
                });
    }

    @Test
    void updateItemExpTest() {
        itemRequestService.delete(itemRequest.getId());
        assertThrows(NotFoundData.class,
                () -> itemRequestService.find(itemRequest.getId()));
    }

    @BeforeEach
    public void init() {
        createUser();
    }

    @AfterEach
    public void clean() {
        em.createNativeQuery("delete from booking").executeUpdate();
        em.createNativeQuery("delete from items");
        em.createNativeQuery("delete from users");
        cleanRequests();
    }

    public void cleanRequests() {
        em.createNativeQuery("delete from requests").executeUpdate();
    }

    public void createUser() {
        userOwner = User.builder()
                .id(3)
                .name("testNameedde")
                .email("testdeded@email.com")
                .build();
        em.createNativeQuery("Insert into Users (id, name, email) values (:id,:name,:email)")
                .setParameter("id", userOwner.getId())
                .setParameter("name", userOwner.getName())
                .setParameter("email", userOwner.getEmail())
                .executeUpdate();

        user = User.builder()
                .id(4)
                .name("testNameItemReq")
                .email("testItemReqd@email.com")
                .build();
        em.createNativeQuery("Insert into Users (id, name, email) values (:id,:name,:email)")
                .setParameter("id", user.getId())
                .setParameter("name", user.getName())
                .setParameter("email", user.getEmail())
                .executeUpdate();

        itemRequest = ItemRequest.builder()
                .id(1)
                .description("test")
                .requestor(userOwner.getId())
                .created(LocalDateTime.now())
                .build();

        em.createNativeQuery("Insert into Requests (id, description, requestor, created) values (:id,:description,:requestor, :created)")
                .setParameter("id", itemRequest.getId())
                .setParameter("description", itemRequest.getDescription())
                .setParameter("requestor", itemRequest.getRequestor())
                .setParameter("created", itemRequest.getCreated())
                .executeUpdate();
    }

}
