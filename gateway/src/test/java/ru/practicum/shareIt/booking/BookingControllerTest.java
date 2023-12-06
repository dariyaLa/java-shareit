package ru.practicum.shareIt.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareIt.items.ItemDto;
import ru.practicum.shareIt.users.UserDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
class BookingControllerTest {

    private static final String URL = "/bookings";

    private static final String HEADERS_USER = "X-Sharer-User-Id";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    BookingClient client;

    @Autowired
    private MockMvc mvc;

    private BookingDto bookingDto;
    private BookingDto bookingDtoStartNull;
    private ItemDto itemDto;
    private UserDto userDto;

    @Test
    @SneakyThrows
    void createBookingInPastTest() {
        String json = mapper.writeValueAsString(bookingDtoStartNull);
        mvc.perform(post(URL)
                        .header(HEADERS_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void createBookingTest() {
        String json = mapper.writeValueAsString(bookingDto);
        mvc.perform(post(URL)
                        .header(HEADERS_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void updateTest() {
        String json = mapper.writeValueAsString(bookingDto);
        mvc.perform(patch(URL + "/1?approved=true")
                        .header(HEADERS_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void findIdTest() {
        mvc.perform(get(URL + "/1")
                        .header(HEADERS_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void findAllTest() {
        mvc.perform(get(URL)
                        .header(HEADERS_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void findInvalidStateTest() {
        mvc.perform(get(URL + "?state=qwert")
                        .header(HEADERS_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    @Test
    @SneakyThrows
    void findAllOwnerTest() {
        mvc.perform(get(URL + "/owner")
                        .header(HEADERS_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void findAllOwnerStateTest() {
        mvc.perform(get(URL + "/owner?state=qwert")
                        .header(HEADERS_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isInternalServerError());
    }

    private void createTestData() {
        userDto = UserDto.builder()
                .id(1)
                .name("testName")
                .email("testdeded@email.com")
                .build();
        itemDto = ItemDto.builder()
                .id(1)
                .name("testName")
                .description("testDescription")
                .available(true)
                .owner(userDto.getId())
                .build();
        bookingDto = BookingDto.builder()
                .id(1)
                .start(LocalDateTime.now().plusMinutes(20))
                .end(LocalDateTime.now().plusMinutes(40))
                .itemId(itemDto.getId())
                .bookerId(userDto.getId())
                .state(State.APPROVED)
                .build();
        bookingDtoStartNull = BookingDto.builder()
                .id(1)
                .start(null)
                .end(LocalDateTime.now().minusMinutes(3))
                .itemId(itemDto.getId())
                .bookerId(userDto.getId())
                .state(State.APPROVED)
                .build();
    }

    @BeforeEach
    void setUp() {
        createTestData();
    }

}