package edu.eci.userService.validation;

import java.util.regex.Pattern;

public class ValidationUtils {

    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-ZáéíóúÁÉÍÓÚ\\s'-]+$");
    private static final Pattern VALID_EMAIL_DOMAINS = Pattern.compile(
            "^[\\w.-]+@(mail\\.escuelaing\\.edu\\.co|escuelaing\\.edu\\.co|gmail\\.com)$");
    private static final int MIN_AGE = 18;
    private static final int MAX_AGE = 100;
    private static final int MIN_STATURE_CM = 100;
    private static final int MAX_STATURE_CM = 300;
    private static final int MIN_DORSAL = 0;
    private static final int MAX_DORSAL = 99;

    public static boolean isValidName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        return NAME_PATTERN.matcher(name.trim()).matches();
    }

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        return VALID_EMAIL_DOMAINS.matcher(email.toLowerCase()).matches();
    }

    public static boolean isValidAge(int age) {
        return age >= MIN_AGE && age <= MAX_AGE;
    }

    public static boolean isValidStature(int statureCm) {
        return statureCm >= MIN_STATURE_CM && statureCm <= MAX_STATURE_CM;
    }

    public static boolean isValidDorsal(int dorsal) {
        return dorsal >= MIN_DORSAL && dorsal <= MAX_DORSAL;
    }

    public static boolean isValidPosition(String position) {
        if (position == null || position.trim().isEmpty()) {
            return false;
        }
        // No caracteres especiales, solo letras y espacios
        return position.matches("^[a-zA-ZáéíóúÁÉÍÓÚ\\s-]+$");
    }

    public static String getValidationErrorMessage(String field, String condition) {
        return switch (field.toLowerCase()) {
            case "name" -> "El nombre debe contener solo letras, espacios, apóstrofes y guiones.";
            case "email" -> "El correo debe terminar en @mail.escuelaing.edu.co, @escuelaing.edu.co o @gmail.com";
            case "age" -> String.format("La edad debe estar entre %d y %d años.", MIN_AGE, MAX_AGE);
            case "stature" -> String.format("La estatura debe estar entre %d y %d cm.", MIN_STATURE_CM, MAX_STATURE_CM);
            case "dorsal" -> String.format("El dorsal debe estar entre %d y %d.", MIN_DORSAL, MAX_DORSAL);
            case "position" -> "La posición debe contener solo letras, espacios y guiones.";
            default -> "Validación inválida para el campo: " + field;
        };
    }
}
