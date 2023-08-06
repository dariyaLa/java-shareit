package ru.practicum.shareit.itemTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import ru.practicum.shareit.item.ItemController;
import ru.practicum.shareit.item.ItemService;
import ru.practicum.shareit.item.dto.CommentsDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.UserDto;
import ru.practicum.shareit.user.UserServiceImpl;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@PropertySource("classpath:application.properties")
public class ItemControllerTest {

    @Mock
    private ItemService itemService;

    @Mock
    private UserServiceImpl userService;

    @InjectMocks
    private ItemController controller;

    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    private ItemDto itemDto;

    private UserDto userDto;

    private Collection<ItemDto> itemDtoCollection;

    private CommentsDto commentDto;

    @BeforeEach
    void setUp() {
        mvc = MockMvcBuilders
                .standaloneSetup(controller)
                .build();

        itemDto = ItemDto.builder()
                .id(1L)
                .name("testName")
                .description("testDescription")
                .available(true)
                .owner(1L)
                .build();

        userDto = UserDto.builder()
                .id(1)
                .name("John")
                .email("john.doe@mail.com")
                .build();

        commentDto = CommentsDto.builder()
                .text("comment text")
                .authorName(userDto.getName())
                //.created(LocalDateTime.now())
                .build();

        itemDtoCollection = Arrays.asList(itemDto);

    }

    @Test
    void createItemTest() throws Exception {
        when(itemService.save(any()))
                .thenReturn(java.util.Optional.ofNullable(itemDto));

        when(userService.find(anyLong()))
                .thenReturn(java.util.Optional.ofNullable(userDto));

        mvc.perform(post("/items")
                        .header("${headers.userId}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    void updateItemTest() throws Exception {
        when(itemService.find(anyLong()))
                .thenReturn(java.util.Optional.ofNullable(itemDto));

        when(itemService.save(any()))
                .thenReturn(java.util.Optional.ofNullable(itemDto));

        mvc.perform(patch("/items/{itemId}", 1)
                        .header("${headers.userId}", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void getItemExpTest() {
        assertThatThrownBy(
                () -> mvc.perform(get("/items/{itemId}", 1)
                                .header("${headers.userId}", 1)
                                .content(mapper.writeValueAsString(itemDto))
                                .characterEncoding(StandardCharsets.UTF_8)
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON))).getCause().hasMessage("Not found data");
    }

    @Test
    @SneakyThrows
    void createItemExpTest() {
        assertThatThrownBy(
                () -> mvc.perform(post("/items", 1)
                        .header("${headers.userId}", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))).getCause().hasMessage("Not found user");
    }

    @Test
    @SneakyThrows
    void updateItemExpTest() {
        assertThatThrownBy(
                () -> mvc.perform(patch("/items/{id}", 1)
                        .header("${headers.userId}", 1)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))).getCause().hasMessage("Not found user");
    }

    @Test
    @SneakyThrows
    void findItemTest() {
        when(itemService.findWihtUserId(anyLong(), anyLong()))
                .thenReturn(java.util.Optional.ofNullable(itemDto));

        mvc.perform(get("/items/{id}", 1)
                        .header("${headers.userId}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(itemDto.getId()), Long.class))
                .andExpect(jsonPath("$.name", is(itemDto.getName())))
                .andExpect(jsonPath("$.description", is(itemDto.getDescription())));
    }

    @Test
    @SneakyThrows
    void findAllItemTest() {
        when(itemService.findAllWithUser(anyLong()))
                .thenReturn(itemDtoCollection);

        mvc.perform(get("/items")
                        .header("${headers.userId}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(itemDtoCollection.size())));
    }

    @Test
    @SneakyThrows
    void searchItemTest() {
        when(itemService.findAll(any()))
                .thenReturn(itemDtoCollection);

        mvc.perform(get("/items/search")
                        .param("text", "test")
                        .header("${headers.userId}", 1L)
                        .content(mapper.writeValueAsString(itemDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()", is(itemDtoCollection.size())));
    }

    @Test
    @SneakyThrows
    void createCommentTest() {
        when(itemService.saveComment(any()))
                .thenReturn(commentDto);

        mvc.perform(post("/items/{itemId}/comment", 1L)
                        .header("${headers.userId}", 1L)
                        .content(mapper.writeValueAsString(commentDto))
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(commentDto.getId()), Long.class))
                .andExpect(jsonPath("$.authorName", is(commentDto.getAuthorName())));
    }


}
