package ru.practicum.shareIt.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private static final String URL = "/users";

    @Autowired
    ObjectMapper mapper;

    @MockBean
    UserClient client;

    @Autowired
    private MockMvc mvc;

    private UserDto userDto;

    private UserDto userNameFail;

    private UserDto userEmailFail;

    @Test
    void shouldCreateMockMvc() {
        assertNotNull(mvc);
    }


    @Test
    @SneakyThrows
    void createUserFailEmailTest() {
        String json = mapper.writeValueAsString(userEmailFail);
        mvc.perform(post(URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    @SneakyThrows
    void update() {
        String json = mapper.writeValueAsString(userDto);
        mvc.perform(patch(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void find() {
        mvc.perform(get(URL + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @SneakyThrows
    void deleteUser() {
        mvc.perform(delete(URL + "/1"))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

    @BeforeEach
    void setUp() {
        createTestData();
    }

    public void createTestData() {
        userDto = UserDto.builder()
                .id(1)
                .name("Jone")
                .email("john.doe@mail.com")
                .build();

        userNameFail = UserDto.builder()
                .id(1)
                .name("")
                .email("john.doe@mail.com")
                .build();

        userEmailFail = UserDto.builder()
                .id(1)
                .name("Jone")
                .email("")
                .build();
    }

}