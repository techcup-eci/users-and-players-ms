package edu.eci.userService.exception;

public class PlayerAlreadyAssignedToTeamException extends RuntimeException {
    public PlayerAlreadyAssignedToTeamException(String message) {
        super(message);
    }
}
