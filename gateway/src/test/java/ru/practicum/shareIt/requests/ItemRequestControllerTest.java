package ru.practicum.shareIt.requests;

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
import ru.practicum.shareIt.items.CommentsDto;
import ru.practicum.shareIt.items.ItemDto;
import ru.practicum.shareIt.users.UserDto;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ItemRequestController.class)
class ItemRequestControllerTest {

    private static final String URL = "/requests";

    private static final String HEADER_USER = "X-Sharer-User-Id";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    ItemRequestClient client;

    @Autowired
    private MockMvc mvc;

    private BookingDto bookingDto;
    private ItemDto itemDto;
    private UserDto userDto;
    private CommentsDto commentsDto;
    private ItemRequestDto itemRequestDtoWithoutDesc;

    @Test
    @SneakyThrows
    void createItemReqWithoutDesc() {
        String json = mapper.writeValueAsString(itemRequestDtoWithoutDesc);
        mvc.perform(post(URL)
                        .header(HEADER_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void findAllByUserId() {
        mvc.perform(get(URL)
                        .header(HEADER_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void findById() {
        mvc.perform(get(URL + "/1")
                        .header(HEADER_USER, "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @BeforeEach
    void setUp() {
        createTestData();
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
        itemRequestDtoWithoutDesc = ItemRequestDto.builder()
                .id(1)
                .description(null)
                .requestor(userDto.getId())
                .created(LocalDateTime.now())
                .items(new ArrayList<>())
                .build();
    }
}