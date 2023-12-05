package ru.practicum.shareIt;

import org.junit.jupiter.api.Test;
import ru.practicum.shareIt.exception.ConflictData;

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
