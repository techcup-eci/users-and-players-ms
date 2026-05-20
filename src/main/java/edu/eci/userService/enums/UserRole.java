package edu.eci.userService.enums;

public enum UserRole {
    STUDENT,
    GRADUATE,
    PROFESSOR,
    ADMINISTRATIVE_STAFF,
    FAMILY_MEMBER,
    ADMINISTRATOR,
    ORGANIZER;

    @Override
    public String toString() {
        return this.name();
    }
}
