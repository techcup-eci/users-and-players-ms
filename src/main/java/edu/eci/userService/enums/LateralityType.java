package edu.eci.userService.enums;

public enum LateralityType {
    LEFT, // Zurdo
    RIGHT, // Derecho
    AMBIDEXTROUS; // Ambidiestro

    @Override
    public String toString() {
        return this.name();
    }
}
