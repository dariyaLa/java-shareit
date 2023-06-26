package ru.practicum.shareit.exeption;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class ExpHandler {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exeptionHandleConflict(final ConflictData e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("messageError", e.getMessage()),
                HttpStatus.CONFLICT
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exeptionHandleIncorrect(final MethodArgumentNotValidException e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("messageError", Objects.requireNonNull(e.getAllErrors().get(0).getDefaultMessage())),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exeptionHandleNotFound(final NotFoundData e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("messageError", e.getMessage()),
                HttpStatus.NOT_FOUND
        );
    }
}
