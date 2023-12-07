package ru.practicum.shareIt.exception;

public class ValidationError extends RuntimeException {
    public ValidationError(String message) {
        super(message);
    }
}
