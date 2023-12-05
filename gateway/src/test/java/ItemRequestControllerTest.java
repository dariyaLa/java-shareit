//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import lombok.SneakyThrows;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//import ru.practicum.shareit.exeption.ConflictDataBooking;
//import ru.practicum.shareit.request.dto.ItemRequestDto;
//import ru.practicum.shareit.user.UserDto;
//import ru.practicum.shareit.user.UserServiceImpl;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//
//import static org.assertj.core.api.Assertions.assertThatThrownBy;
//import static org.hamcrest.Matchers.is;
//import static org.junit.jupiter.api.Assertions.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//@PropertySource("classpath:application1.properties")
//public class ItemRequestControllerTest {
//
//    @Mock
//    private ItemRequestServiceImpl itemService;
//
//    @Mock
//    private UserServiceImpl userService;
//
//    @InjectMocks
//    private ItemRequestController controller;
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @Autowired
//    private MockMvc mvc;
//
//    private UserDto userDto;
//
//    private ItemRequestDto itemRequestDto;
//
//    private Collection<ItemRequestDto> itemRequestDtoCollection;
//
//
//    @Test
//    void createItemReqTest() throws Exception {
//        when(itemService.save(any()))
//                .thenReturn(java.util.Optional.ofNullable(itemRequestDto));
//
//        when(userService.find(anyLong()))
//                .thenReturn(java.util.Optional.ofNullable(userDto));
//
//        mvc.perform(post("/ru.practicum.booking.requests")
//                        .header("${headers.userId}", 1L)
//                        .content(mapper.writeValueAsString(itemRequestDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
//                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
//    }
//
//    @Test
//    void findAllItemReqTest() throws Exception {
//        when(itemService.findAll(anyLong(), any()))
//                .thenReturn(itemRequestDtoCollection);
//
//        when(userService.find(anyLong()))
//                .thenReturn(java.util.Optional.ofNullable(userDto));
//
//        mvc.perform(get("/ru.practicum.booking.requests")
//                        .header("${headers.userId}", 1L)
//                        .content(mapper.writeValueAsString(itemRequestDtoCollection))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(itemRequestDtoCollection.size())));
//    }
//
//    @Test
//    void findItemReqTest() throws Exception {
//        when(itemService.find(anyLong()))
//                .thenReturn(java.util.Optional.ofNullable(itemRequestDto));
//
//        when(userService.find(anyLong()))
//                .thenReturn(java.util.Optional.ofNullable(userDto));
//
//        mvc.perform(get("/ru.practicum.booking.requests/{id}", itemRequestDto.getId())
//                        .header("${headers.userId}", 1L)
//                        .content(mapper.writeValueAsString(itemRequestDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(itemRequestDto.getId()), Long.class))
//                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));
//    }
//
//    @Test
//    void findAllWithoutOwnerItemReqTest() throws Exception {
//        when(itemService.findAllWithoutOwner(anyLong(), any()))
//                .thenReturn(itemRequestDtoCollection);
//
//        when(userService.find(anyLong()))
//                .thenReturn(java.util.Optional.ofNullable(userDto));
//
//        mvc.perform(get("/ru.practicum.booking.requests/all", itemRequestDto.getId())
//                        .header("${headers.userId}", 1L)
//                        .content(mapper.writeValueAsString(itemRequestDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(itemRequestDtoCollection.size())));
//    }
//
//    @Test
//    @SneakyThrows
//    void createItemReqExpTest() {
//        assertThatThrownBy(
//                () -> mvc.perform(get("/ru.practicum.booking.requests")
//                                .header("${headers.userId}", 1L)
//                                .content(mapper.writeValueAsString(itemRequestDto))
//                                .characterEncoding(StandardCharsets.UTF_8)
//                                .contentType(MediaType.APPLICATION_JSON)
//                                .accept(MediaType.APPLICATION_JSON))
//                        .andExpect(result -> assertTrue(result.getResolvedException() instanceof ConflictDataBooking)));
//    }
//
//    @Test
//    @SneakyThrows
//    void getItemExpTest() {
//        assertThatThrownBy(
//                () -> mvc.perform(get("/ru.practicum.booking.requests/{id}", 1)
//                        .header("${headers.userId}", 1)
//                        .content(mapper.writeValueAsString(itemRequestDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))).getCause().hasMessage("Not found user");
//    }
//
//    @Test
//    @SneakyThrows
//    void createItemExpTest() {
//        assertThatThrownBy(
//                () -> mvc.perform(post("/ru.practicum.booking.requests")
//                        .header("${headers.userId}", 1)
//                        .content(mapper.writeValueAsString(itemRequestDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))).getCause().hasMessage("Not found user");
//    }
//
//    @BeforeEach
//    void setUp() {
//
//        mapper.registerModule(new JavaTimeModule());
//
//        mvc = MockMvcBuilders
//                .standaloneSetup(controller)
//                .build();
//
//        userDto = UserDto.builder()
//                .id(1)
//                .name("John")
//                .email("john.doe@mail.com")
//                .build();
//
//        itemRequestDto = ItemRequestDto.builder()
//                .id(1)
//                .description("test")
//                .requestor(1L)
//                .created(LocalDateTime.now())
//                .ru.practicum.shareIt.gateway.users.items(new ArrayList<>())
//                .build();
//
//        itemRequestDtoCollection = Arrays.asList(itemRequestDto);
//    }
//}
