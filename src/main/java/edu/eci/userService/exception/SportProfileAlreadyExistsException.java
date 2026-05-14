package edu.eci.userService.exception;

public class SportProfileAlreadyExistsException extends RuntimeException {
    public SportProfileAlreadyExistsException(String message) {
        super(message);
    }
}
