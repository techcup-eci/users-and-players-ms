package edu.eci.userService.enums;

public enum UserRole {
    STUDENT,
    GRADUATE,
    TEACHER,
    ADMINISTRATIVE_STAFF,
    ADMINISTRATOR,
    ORGANIZER,
    OTHER;
     

    @Override
    public String toString() {
        return this.name();
    }
}
