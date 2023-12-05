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
//import ff.ru.practicum.booking.dto.BookingDtoOut;
//import ru.practicum.shareit.item.ItemService;
//import ru.practicum.shareit.item.dto.ItemDto;
//import ru.practicum.shareit.user.UserDto;
//import ru.practicum.shareit.user.UserServiceImpl;
//
//import java.nio.charset.StandardCharsets;
//import java.time.LocalDateTime;
//import java.util.Arrays;
//import java.util.Collection;
//
//import static org.hamcrest.Matchers.is;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.when;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ExtendWith(MockitoExtension.class)
//@PropertySource("classpath:application1.properties")
//public class BookingControllerTest {
//
//    @InjectMocks
//    private BookingController controller;
//
//    private final ObjectMapper mapper = new ObjectMapper();
//
//    @Autowired
//    private MockMvc mvc;
//
//
//    @Mock
//    private BookingService bookingService;
//
//    private BookingDtoOut bookingDto;
//
//    @Mock
//    private ItemDto itemDto;
//
//    @Mock
//    private UserDto userDto;
//
//    private UserServiceImpl userService;
//
//    private ItemService itemService;
//
//    private Collection<BookingDtoOut> bookings;
//
//    @Test
//    @SneakyThrows
//    void createBookingTest() {
//        when(bookingService.save(any()))
//                .thenReturn(bookingDto);
//
//        mvc.perform(post("/bookings")
//                        .header("${headers.userId}", 1L)
//                        .content(mapper.writeValueAsString(bookingDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
//                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
//                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
//    }
//
//    @Test
//    @SneakyThrows
//    void updateBookingTest() {
//        when(bookingService.update(anyLong(), anyBoolean(), anyLong()))
//                .thenReturn(bookingDto);
//
//        mvc.perform(patch("/bookings/{bookingId}", bookingDto.getId())
//                        .header("${headers.userId}", 1L)
//                        .param("approved", "true")
//                        .content(mapper.writeValueAsString(bookingDto))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
//                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
//                .andExpect(jsonPath("$.status", is(State.APPROVED.name())));
//    }
//
//    @Test
//    @SneakyThrows
//    void findBookingTest() {
//        when(bookingService.find(anyLong(), anyLong()))
//                .thenReturn(bookingDto);
//
//        mvc.perform(get("/bookings/{bookingId}", bookingDto.getId())
//                        .header("${headers.userId}", 1L)
//                        .content(mapper.writeValueAsString(bookings))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.id", is(bookingDto.getId()), Long.class))
//                .andExpect(jsonPath("$.item.id", is(bookingDto.getItem().getId()), Long.class))
//                .andExpect(jsonPath("$.status", is(bookingDto.getStatus().name())));
//    }
//
//
//    @Test
//    @SneakyThrows
//    void findBookingOwnerTest() {
//        when(bookingService.findAllOwner(anyLong(), any(), any()))
//                .thenReturn(bookings);
//
//        mvc.perform(get("/bookings/owner")
//                        /* .param("state", "ALL")
//                         .param("from","0")
//                         .param("size", "2")*/
//                        .header("${headers.userId}", 1L)
//                        .content(mapper.writeValueAsString(bookings))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(bookings.size())));
//    }
//
//    @Test
//    @SneakyThrows
//    void findAllBookingTest() {
//        when(bookingService.findAll(anyLong(), any(), any()))
//                .thenReturn(bookings);
//
//        mvc.perform(get("/bookings")
//                        .header("${headers.userId}", 1L)
//                        .content(mapper.writeValueAsString(bookings))
//                        .characterEncoding(StandardCharsets.UTF_8)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.length()", is(bookings.size())));
//    }
//
//    @BeforeEach
//    void setUp() {
//        mvc = MockMvcBuilders
//                .standaloneSetup(controller)
//                .build();
//
//        mapper.registerModule(new JavaTimeModule());
//
//        itemDto = ItemDto.builder()
//                .id(1L)
//                .name("testName")
//                .description("testDescription")
//                .available(true)
//                .owner(1L)
//                .build();
//
//        userDto = UserDto.builder()
//                .id(1)
//                .name("John")
//                .email("john.doe@mail.com")
//                .build();
//
//        bookingDto = BookingDtoOut.builder()
//                .id(4)
//                .start(LocalDateTime.now().minusMinutes(4))
//                .end(LocalDateTime.now().minusMinutes(3))
//                .item(itemDto)
//                .booker(userDto)
//                .status(State.APPROVED)
//                .build();
//
//        bookings = Arrays.asList(bookingDto);
//
//    }
//}
