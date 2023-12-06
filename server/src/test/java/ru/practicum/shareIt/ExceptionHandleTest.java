package ru.practicum.shareIt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareIt.booking.Booking;
import ru.practicum.shareIt.booking.BookingDto;
import ru.practicum.shareIt.booking.BookingRepository;
import ru.practicum.shareIt.booking.State;
import ru.practicum.shareIt.items.*;
import ru.practicum.shareIt.users.User;
import ru.practicum.shareIt.users.UserRepository;
import ru.practicum.shareIt.users.UserServiceImpl;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@ExtendWith({MockitoExtension.class, SpringExtension.class})
//@EnableAutoConfiguration(exclude = DataSourceAutoConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@RequiredArgsConstructor
@ComponentScan("ru.practicum.shareIt")
//@ContextConfiguration(locations = "classpath*:/spring/applicationContext*.xml")
//@ContextConfiguration(locations = "/test-context.xml")
//@AutoConfigureTestDatabase
public class ExceptionHandleTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Autowired
    private ItemService itemService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private ItemRepository itemRepository;

    private Item item;

    private Item itemSave;

    private User user;

    private Booking booking;

    private Booking bookingApproved;


    @Test
    void notFoundExp() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/items/99";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();

        headers.set("X-Sharer-User-Id", "1");
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<ItemDto> response = restTemplate.exchange(
                uri,
                HttpMethod.GET,
                request,
                ItemDto.class);
        assertThat(response.getStatusCode(), equalTo(HttpStatus.NOT_FOUND));
    }

    @Test
    void stateExp() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/bookings?state=state";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Sharer-User-Id", "1");
        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<ItemDto> response = restTemplate.exchange(uri, HttpMethod.GET, request, ItemDto.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @Test
    void validationExp() throws URISyntaxException {
        final String baseUrl = "http://localhost:" + randomServerPort + "/bookings";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();

        headers.set("X-Sharer-User-Id", "1");
        headers.set("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("start", null);

        HttpEntity<BookingDto> request = new HttpEntity(jsonObject.toString(), headers);
        ResponseEntity<BookingDto> response = restTemplate.postForEntity(uri, request, BookingDto.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void validationDataBookingExp() throws URISyntaxException {
        createUserWithItem();
        final String baseUrl = "http://localhost:" + randomServerPort + "/bookings";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();

        headers.set("X-Sharer-User-Id", "1");
        headers.set("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("itemId", "1");
        jsonObject.put("start", LocalDateTime.now().minusMinutes(5).toString());
        jsonObject.put("end", LocalDateTime.now().minusMinutes(5).toString());

        HttpEntity<BookingDto> request = new HttpEntity(jsonObject.toString(), headers);
        ResponseEntity<BookingDto> response = restTemplate.postForEntity(uri, request, BookingDto.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    @Test
    void validationSaveCommentExpBookingExp() throws URISyntaxException {
        createUserWithItem();
        final String baseUrl = "http://localhost:" + randomServerPort + "/items/" + itemSave.getId() + "/comment";
        URI uri = new URI(baseUrl);
        HttpHeaders headers = new HttpHeaders();

        headers.set("X-Sharer-User-Id", "1");
        headers.set("Content-Type", "application/json");

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("text", "text");


        HttpEntity<BookingDto> request = new HttpEntity(jsonObject.toString(), headers);
        ResponseEntity<Comments> response = restTemplate.postForEntity(uri, request, Comments.class);

        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
    }

    public void createUserWithItem() {
        user = User.builder()
                .id(10)
                .name("testNameedde")
                .email("testdeded@email.com")
                .build();

        User saveUser = userRepository.save(user);

        item = Item.builder()
                .id(10)
                .name("testName")
                .description("testDescription")
                .available(true)
                .owner(saveUser.getId())
                .build();
        itemSave = itemRepository.save(item);
    }

    public void createBooking() {
        booking = Booking.builder()
                .start(LocalDateTime.now().plusMinutes(1))
                .end(LocalDateTime.now().plusMinutes(2))
                .itemId(item.getId())
                .userId(user.getId())
                .state(State.APPROVED)
                .build();

        bookingApproved = bookingRepository.save(booking);
    }

    @AfterEach
    public void clear() {
        bookingRepository.deleteAll();
        itemRepository.deleteAll();
        userRepository.deleteAll();

    }

}
