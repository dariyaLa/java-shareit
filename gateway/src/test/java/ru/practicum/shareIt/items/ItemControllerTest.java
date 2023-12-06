package ru.practicum.shareIt.items;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareIt.booking.BookingDto;
import ru.practicum.shareIt.booking.State;
import ru.practicum.shareIt.users.UserDto;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    private static final String URL = "/items";

    private static final String HEADER_USER = "X-Sharer-User-Id";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemClient client;

    @Autowired
    private MockMvc mvc;

    private BookingDto bookingDto;
    private ItemDto itemDto;
    private UserDto userDto;
    private CommentsDto commentsDto;

    @Test
    @SneakyThrows
    void createWithoutHeaderUserTest() {
        String json = mapper.writeValueAsString(itemDto);
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void createItemTest() {
        String json = mapper.writeValueAsString(itemDto);
        mvc.perform(post(URL)
                        .header(HEADER_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void updateTest() {
        String json = mapper.writeValueAsString(itemDto);
        mvc.perform(patch(URL + "/1")
                        .header(HEADER_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void findAIdTest() {
        mvc.perform(get(URL + "/1")
                        .header(HEADER_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void findAAllTest() {
        mvc.perform(get(URL)
                        .header(HEADER_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void searchItemTest() {
        mvc.perform(get(URL + "/search?text=оТверТ")
                        .header(HEADER_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void createCommentTest() {
        String json = mapper.writeValueAsString(commentsDto);
        mvc.perform(post(URL + "/1/comment")
                        .header(HEADER_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
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
        commentsDto = CommentsDto.builder()
                .text("test")
                .authorName(userDto.getName())
                .created(LocalDateTime.now())
                .build();
    }

    @BeforeEach
    void setUp() {
        createTestData();
    }
}