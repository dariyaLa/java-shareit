package ru.practicum.shareit.exeption;

public class ExeptionBadRequest extends RuntimeException {

    public ExeptionBadRequest(String message) {
        super(message);
    }
}
