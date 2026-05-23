package edu.eci.userService.enums;

public enum SchoolRelation {
    STUDENT,
    PROFESSOR,
    TEACHER,
    GRADUATE,
    STAFF,
    FAMILY;

    @Override
    public String toString() {
        return this.name();
    }
}
