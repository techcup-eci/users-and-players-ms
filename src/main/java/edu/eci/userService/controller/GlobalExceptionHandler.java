package edu.eci.userService.controller;

import edu.eci.userService.exception.DeleteSportProfileNotAllowedException;
import edu.eci.userService.exception.PendingJoinRequestException;
import edu.eci.userService.exception.PlayerAlreadyAssignedToTeamException;
import edu.eci.userService.exception.SportProfileAlreadyExistsException;
import edu.eci.userService.exception.TeamNotAvailableException;
import edu.eci.userService.exception.UserLinkedToActiveTournamentException;
import edu.eci.userService.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

/**
 * Global exception handler that maps domain exceptions to HTTP status codes.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleUserNotFound(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(UserLinkedToActiveTournamentException.class)
    public ResponseEntity<Map<String, String>> handleLinkedToTournament(
            UserLinkedToActiveTournamentException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(PendingJoinRequestException.class)
    public ResponseEntity<Map<String, String>> handlePendingRequest(PendingJoinRequestException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(SportProfileAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleProfileAlreadyExists(
            SportProfileAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(PlayerAlreadyAssignedToTeamException.class)
    public ResponseEntity<Map<String, String>> handlePlayerAssigned(
            PlayerAlreadyAssignedToTeamException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(TeamNotAvailableException.class)
    public ResponseEntity<Map<String, String>> handleTeamNotAvailable(TeamNotAvailableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(DeleteSportProfileNotAllowedException.class)
    public ResponseEntity<Map<String, String>> handleDeleteNotAllowed(
            DeleteSportProfileNotAllowedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(
            MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult().getFieldErrors().stream()
            .findFirst()
            .map(fe -> fe.getDefaultMessage())
            .orElse("Validation error");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", message));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(
            MethodArgumentTypeMismatchException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "Invalid parameter type: " + ex.getName()));
    }
}
