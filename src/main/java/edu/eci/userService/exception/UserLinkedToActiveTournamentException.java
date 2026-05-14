package edu.eci.userService.exception;

public class UserLinkedToActiveTournamentException extends RuntimeException {
    public UserLinkedToActiveTournamentException(String message) {
        super(message);
    }
}
