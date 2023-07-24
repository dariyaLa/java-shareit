package ru.practicum.shareit.exeption;

public class ConflictDataBooking extends RuntimeException {

    public ConflictDataBooking(String message) {
        super(message);
    }
}
