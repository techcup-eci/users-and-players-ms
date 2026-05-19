package edu.eci.userService.enums;

public enum UserStatus {
    ACTIVE,
    INACTIVE;

    @Override
    public String toString() {
        return this.name();
    }
}
