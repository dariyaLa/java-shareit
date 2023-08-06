package ru.practicum.shareit.exp;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.exeption.ConflictData;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class ExceptoinTest {

    @Test
    public void conflictDataTest() {
        ConflictData conflictData = new ConflictData("test");

        assertNotNull(conflictData);
        assertEquals(conflictData.getMessage(), "test");

    }

}
