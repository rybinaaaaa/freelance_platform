package freelanceplatform.controllers.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * Global exception handler for controllers.
 * This class extends {@link ResponseEntityExceptionHandler} and handles exceptions
 * thrown by any controller within the application.
 */
@ControllerAdvice
@Slf4j
public class ControllerManager extends ResponseEntityExceptionHandler {

    /**
     * Handles generic exceptions.
     * Logs the exception message and returns a {@link HttpStatus#BAD_REQUEST} response.
     *
     * @param e the exception that was thrown
     * @return a {@link ResponseEntity} with {@link HttpStatus#BAD_REQUEST}
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<HttpStatus> handleException(Exception e) {
        log.error("An error occurred: ", e);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }
}