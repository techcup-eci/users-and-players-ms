package edu.eci.userService.enums;

public enum AcademicLevel {
    UNDERGRADUATE, // Pregrado
    SPECIALIZATION, // Especialización
    MASTER, // Maestría
    DOCTORATE; // Doctorado

    @Override
    public String toString() {
        return this.name();
    }
}
