package edu.eci.userService.exception;

public class PendingJoinRequestException extends RuntimeException {
    
    public PendingJoinRequestException(String message) {
        super(message);
    }

    public PendingJoinRequestException(String message, Throwable cause) {
        super(message, cause);
    }
}
