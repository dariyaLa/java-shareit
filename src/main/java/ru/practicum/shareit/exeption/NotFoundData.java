package ru.practicum.shareit.exeption;

public class NotFoundData extends RuntimeException {
    public NotFoundData(String message) {
        super(message);
    }

}
