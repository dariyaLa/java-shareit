package ru.practicum.shareit.exeption;

public class ValidationError extends RuntimeException {
    public ValidationError(String message) {
        super(message);
    }
}
