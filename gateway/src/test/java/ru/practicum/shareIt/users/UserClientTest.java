//package ru.practicum.shareIt.users;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.setup.MockMvcBuilders;
//
//@ExtendWith(MockitoExtension.class)
//public class UserClientTest {
//
//    private static final String URL = "/users";
//
//    @Autowired
//    ObjectMapper mapper;
//
//    @Autowired
//    private MockMvc mvc;
//
//    @InjectMocks
//    UserClient client;
//
//    private UserDto userDto;
//
//    private UserDto userNameFail;
//
//    private UserDto userEmailFail;
//
////    @Test
////    void shouldCreateMockMvc() {
////        assertNotNull(mvc);
////    }
//
//
//    @Test
//    void findUsers() {
////        when(client.findUsers())
////                .thenReturn(ResponseEntity.accepted().body(userDto));
////        ResponseEntity<Object> response = client.findUsers();
////        Assertions.assertNotNull(response.getStatusCode());
//    }
//
//    @Test
//    void createUser() {
//    }
//
//    @Test
//    void findUserById() {
//    }
//
//    @Test
//    void patch() {
//    }
//
//    @Test
//    void delete() {
//    }
//
//    @BeforeEach
//    void setUp() {
//        mvc = MockMvcBuilders
//                .standaloneSetup(client)
//                .build();
//
//        mapper.registerModule(new JavaTimeModule());
//
//        userDto = UserDto.builder()
//                .id(1)
//                .name("John")
//                .email("john.doe@mail.com")
//                .build();
//    }
//
//    @AfterEach
//    void tearDown() {
//    }
//}
