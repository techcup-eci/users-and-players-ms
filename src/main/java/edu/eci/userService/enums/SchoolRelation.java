package edu.eci.userService.enums;

public enum SchoolRelation {
    STUDENT,
    PROFESSOR,
    GRADUATE;

    @Override
    public String toString() {
        return this.name();
    }
}
