package ru.practicum.shareIt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

@Slf4j
@AutoConfigureTestDatabase
@SpringBootTest()
public class ShareItTests {

    @Autowired
    private ApplicationContext context;

    @Test
    void contextLoads() {

    }
}
