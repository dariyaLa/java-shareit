package ru.practicum.shareIt.exception;

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
                Map.of("messageError", "Поле " + Objects.requireNonNull(Objects.requireNonNull(e.getFieldError()).getField()) +
                        " " + Objects.requireNonNull(e.getAllErrors().get(0).getDefaultMessage())),
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

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exeptionHandleConflictBooking(final ConflictDataBooking e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("messageError", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exeptionHandleValidationBooking(final ValidationError e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("messageError", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

//    @ExceptionHandler
//    public ResponseEntity<Map<String, String>> exeptionHandleState(final State.ExeptionState e) {
//        return new ResponseEntity<Map<String, String>>(
//                Map.of("error", e.getMessage()),
//                HttpStatus.BAD_REQUEST
//        );
//    }

    @ExceptionHandler
    public ResponseEntity<Map<String, String>> exeptionHandleBadRequest(final ExeptionBadRequest e) {
        return new ResponseEntity<Map<String, String>>(
                Map.of("error", e.getMessage()),
                HttpStatus.BAD_REQUEST
        );
    }

}
