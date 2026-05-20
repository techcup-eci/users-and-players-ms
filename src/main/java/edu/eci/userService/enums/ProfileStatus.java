package edu.eci.userService.enums;

public enum ProfileStatus {
    ACTIVE,
    INACTIVE;

    @Override
    public String toString() {
        return this.name();
    }
}
