package edu.eci.userService.exception;

public class DeleteSportProfileNotAllowedException extends RuntimeException {
    public DeleteSportProfileNotAllowedException(String message) {
        super(message);
    }
}
