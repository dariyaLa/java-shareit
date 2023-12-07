package ru.practicum.shareIt.exception;

public class ConflictDataBooking extends RuntimeException {

    public ConflictDataBooking(String message) {
        super(message);
    }
}
