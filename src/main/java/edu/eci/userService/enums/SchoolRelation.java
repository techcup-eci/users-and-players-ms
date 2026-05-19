package edu.eci.userService.enums;

public enum SchoolRelation {
    STUDENT,
    PROFESSOR,
    GRADUATE,
    GUEST,
    ADMINISTRATIVE_STAFF,
    FAMILY_MEMBER;

    @Override
    public String toString() {
        return this.name();
    }
}
