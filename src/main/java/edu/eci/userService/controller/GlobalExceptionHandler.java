package edu.eci.userService.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import edu.eci.userService.exception.PendingJoinRequestException;
import edu.eci.userService.exception.TeamNotAvailableException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.exceptions.InvalidCredentialsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(NoSuchElementException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Resource not found";
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", message));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleBadRequest(IllegalArgumentException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Bad request";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", message));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleConflict(IllegalStateException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Conflict";
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("error", message));
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<Map<String, String>> handleInvalidCredentials(InvalidCredentialsException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Invalid credentials";
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("error", message));
    }

    // NUEVO — soluciona el error "No enum constant SchoolRelation.student"
    @ExceptionHandler(HttpMessageConversionException.class)
    public ResponseEntity<Map<String, String>> handleEnumConversion(HttpMessageConversionException ex) {
        String message = "Invalid value in request body. Check that enum fields use uppercase values " +
                "(e.g. STUDENT, PROFESSOR, UNDERGRADUATE).";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", message));
    }

    // NUEVO — captura UserNotFoundException, PendingJoinRequestException,
    // TeamNotAvailableException
    @ExceptionHandler({ UserNotFoundException.class,
            PendingJoinRequestException.class,
            TeamNotAvailableException.class })
    public ResponseEntity<Map<String, String>> handleDomainExceptions(RuntimeException ex) {
        String message = ex.getMessage() != null ? ex.getMessage() : "Operation failed";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", message));
    }
}