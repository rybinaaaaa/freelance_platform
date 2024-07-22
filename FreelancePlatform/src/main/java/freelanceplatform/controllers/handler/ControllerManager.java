package freelanceplatform.controllers.handler;

import freelanceplatform.exceptions.ErrorResponse;
import freelanceplatform.exceptions.NotFoundException;
import freelanceplatform.exceptions.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * Global exception handler for controllers.
 * This class extends {@link ResponseEntityExceptionHandler} and handles exceptions
 * thrown by any controller within the application.
 */
@ControllerAdvice
@Slf4j
public class ControllerManager extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleException(NotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.NOT_FOUND);
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException e) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setStatus(HttpStatus.CONFLICT);
        errorResponse.setMessage(e.getMessage());

        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
}