package ru.practicum.shareit.exp;

import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.ItemDto;

import java.net.URI;
import java.net.URISyntaxException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@ExtendWith({MockitoExtension.class, SpringExtension.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ExceptionHandleTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    int randomServerPort;

    @Mock
    private ItemService itemService;

    private ItemDto itemDto;


    @BeforeEach
    void setUp() {
        itemDto = ItemDto.builder()
                .id(1L)
                .name("testName")
                .description("testDescription")
                .available(true)
                .owner(1L)
                .build();
    }

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

        assertThat(response.getStatusCode(), equalTo(HttpStatus.BAD_REQUEST));
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

}
