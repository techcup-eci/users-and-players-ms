package edu.eci.userService.enums;

public enum IdentificationType {
    CC, // Cédula de Ciudadanía
    TI, // Tarjeta de Identidad
    PP, // Pasaporte
    CE, // Cédula de Extranjería
    OTRO; // Otro

    @Override
    public String toString() {
        return this.name();
    }
}
