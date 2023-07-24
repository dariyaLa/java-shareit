package ru.practicum.shareit.exeption;

public class ConflictData extends RuntimeException {

    public ConflictData(String message) {
        super(message);
    }

}
