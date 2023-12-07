package ru.practicum.shareIt;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.practicum.shareIt.booking.State;

import java.util.Map;

@ControllerAdvice
public class Exeption {

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exeptionHandleStateBooking(final State.ExeptionState e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("error", e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}
