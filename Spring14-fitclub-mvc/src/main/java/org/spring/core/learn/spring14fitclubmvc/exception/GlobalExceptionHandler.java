package org.spring.core.learn.spring14fitclubmvc.exception;


import org.spring.core.learn.spring14fitclubmvc.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;




/*
 * ================= REST API: @RestControllerAdvice =================
 * @RestControllerAdvice = @ControllerAdvice + @ResponseBody applied to
 * every method here - a GLOBAL error handler that returns JSON (via
 * ResponseEntity) instead of an HTML error page.
 * ResponseEntity<T> lets us control the EXACT HTTP status code alongside
 * the body - a plain return type always answers 200 OK. Each exception
 * below maps to a specific, semantically correct HTTP status - this
 * mapping IS the API's error contract a React frontend can branch on.
 *ErrorResponse --> DTO in response...

 /
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex)
    {
        return build(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleInvalidCredentials(InvalidCredentialsException ex) {
        return build(HttpStatus.UNAUTHORIZED, ex.getMessage()); // 401
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex.getMessage()); // 404
    }

    @ExceptionHandler(ClassFullException.class)
    public ResponseEntity<ErrorResponse> handleClassFull(ClassFullException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage()); // 409
    }

    @ExceptionHandler(AlreadyBookedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyBooked(AlreadyBookedException ex) {
        return build(HttpStatus.CONFLICT, ex.getMessage()); // 409
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        return build(HttpStatus.valueOf(ex.getStatusCode().value()), ex.getReason());
    }

    private ResponseEntity<ErrorResponse> build(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message, null));
    }


}
