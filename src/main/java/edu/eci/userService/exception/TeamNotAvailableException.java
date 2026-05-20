package edu.eci.userService.exception;

public class TeamNotAvailableException extends RuntimeException {
    
    public TeamNotAvailableException(String message) {
        super(message);
    }

    public TeamNotAvailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
