package edu.eci.userService.enums;

public enum ProfessorType {
    FULL_TIME, // Tiempo completo
    CHAIR; // Catedra

    @Override
    public String toString() {
        return this.name();
    }
}
