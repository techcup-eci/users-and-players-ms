package edu.eci.userService.validation;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("ValidationUtils Tests")
class ValidationUtilsTest {


    @Test
    @DisplayName("isValidName: debe aceptar nombre válido")
    void isValidNameWithValidName() {
        assertThat(ValidationUtils.isValidName("Carlos Pérez")).isTrue();
    }

    @Test
    @DisplayName("isValidName: debe rechazar nombre nulo")
    void isValidNameWithNull() {
        assertThat(ValidationUtils.isValidName(null)).isFalse();
    }

    @Test
    @DisplayName("isValidName: debe rechazar nombre vacío")
    void isValidNameWithEmpty() {
        assertThat(ValidationUtils.isValidName("")).isFalse();
    }

    @Test
    @DisplayName("isValidName: debe rechazar nombre con solo espacios")
    void isValidNameWithBlank() {
        assertThat(ValidationUtils.isValidName("   ")).isFalse();
    }

    @Test
    @DisplayName("isValidName: debe rechazar nombre con números")
    void isValidNameWithNumbers() {
        assertThat(ValidationUtils.isValidName("Carlos123")).isFalse();
    }

    @Test
    @DisplayName("isValidName: debe aceptar nombre con apóstrofe y guion")
    void isValidNameWithApostropheAndHyphen() {
        assertThat(ValidationUtils.isValidName("O'Brien-Smith")).isTrue();
    }

    @Test
    @DisplayName("isValidName: debe aceptar nombre con tildes")
    void isValidNameWithAccents() {
        assertThat(ValidationUtils.isValidName("María José")).isTrue();
    }

    @Test
    @DisplayName("isValidName: debe rechazar nombre con caracteres especiales")
    void isValidNameWithSpecialChars() {
        assertThat(ValidationUtils.isValidName("Carlos@Perez")).isFalse();
    }


    @ParameterizedTest
    @ValueSource(strings = {
            "juan@mail.escuelaing.edu.co",
            "juan@escuelaing.edu.co",
            "juan@gmail.com"
    })
    @DisplayName("isValidEmail: debe aceptar dominios válidos")
    void isValidEmailWithValidDomains(String email) {
        assertThat(ValidationUtils.isValidEmail(email)).isTrue();
    }

    @Test
    @DisplayName("isValidEmail: debe rechazar email nulo")
    void isValidEmailWithNull() {
        assertThat(ValidationUtils.isValidEmail(null)).isFalse();
    }

    @Test
    @DisplayName("isValidEmail: debe rechazar email vacío")
    void isValidEmailWithEmpty() {
        assertThat(ValidationUtils.isValidEmail("")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "juan@hotmail.com",
            "juan@yahoo.com",
            "invalid-email",
            "juan@"
    })
    @DisplayName("isValidEmail: debe rechazar dominios inválidos")
    void isValidEmailWithInvalidDomains(String email) {
        assertThat(ValidationUtils.isValidEmail(email)).isFalse();
    }

    @Test
    @DisplayName("isValidEmail: debe rechazar email con solo espacios")
    void isValidEmailWithBlank() {
        assertThat(ValidationUtils.isValidEmail("   ")).isFalse();
    }


    @ParameterizedTest
    @ValueSource(ints = {18, 50, 100})
    @DisplayName("isValidAge: debe aceptar edades entre 18 y 100")
    void isValidAgeWithValidAges(int age) {
        assertThat(ValidationUtils.isValidAge(age)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 17, 101, 200})
    @DisplayName("isValidAge: debe rechazar edades fuera de rango")
    void isValidAgeWithInvalidAges(int age) {
        assertThat(ValidationUtils.isValidAge(age)).isFalse();
    }

    @Test
    @DisplayName("isValidAge: límite inferior exacto (18) debe ser válido")
    void isValidAgeLowerBoundary() {
        assertThat(ValidationUtils.isValidAge(18)).isTrue();
    }

    @Test
    @DisplayName("isValidAge: límite superior exacto (100) debe ser válido")
    void isValidAgeUpperBoundary() {
        assertThat(ValidationUtils.isValidAge(100)).isTrue();
    }


    @ParameterizedTest
    @ValueSource(ints = {100, 175, 300})
    @DisplayName("isValidStature: debe aceptar estaturas entre 100 y 300 cm")
    void isValidStatureWithValidValues(int stature) {
        assertThat(ValidationUtils.isValidStature(stature)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 99, 301, 500})
    @DisplayName("isValidStature: debe rechazar estaturas fuera de rango")
    void isValidStatureWithInvalidValues(int stature) {
        assertThat(ValidationUtils.isValidStature(stature)).isFalse();
    }

    @Test
    @DisplayName("isValidStature: límite inferior exacto (100) debe ser válido")
    void isValidStatureLowerBoundary() {
        assertThat(ValidationUtils.isValidStature(100)).isTrue();
    }

    @Test
    @DisplayName("isValidStature: límite superior exacto (300) debe ser válido")
    void isValidStatureUpperBoundary() {
        assertThat(ValidationUtils.isValidStature(300)).isTrue();
    }


    @ParameterizedTest
    @ValueSource(ints = {0, 1, 50, 99})
    @DisplayName("isValidDorsal: debe aceptar dorsales entre 0 y 99")
    void isValidDorsalWithValidValues(int dorsal) {
        assertThat(ValidationUtils.isValidDorsal(dorsal)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, 100, 200})
    @DisplayName("isValidDorsal: debe rechazar dorsales fuera de rango")
    void isValidDorsalWithInvalidValues(int dorsal) {
        assertThat(ValidationUtils.isValidDorsal(dorsal)).isFalse();
    }

    @Test
    @DisplayName("isValidDorsal: límite inferior exacto (0) debe ser válido")
    void isValidDorsalLowerBoundary() {
        assertThat(ValidationUtils.isValidDorsal(0)).isTrue();
    }

    @Test
    @DisplayName("isValidDorsal: límite superior exacto (99) debe ser válido")
    void isValidDorsalUpperBoundary() {
        assertThat(ValidationUtils.isValidDorsal(99)).isTrue();
    }


    @ParameterizedTest
    @ValueSource(strings = {"Delantero", "Portero", "Medio-centro"})
    @DisplayName("isValidPosition: debe aceptar posiciones válidas")
    void isValidPositionWithValidValues(String position) {
        assertThat(ValidationUtils.isValidPosition(position)).isTrue();
    }

    @Test
    @DisplayName("isValidPosition: debe rechazar posición nula")
    void isValidPositionWithNull() {
        assertThat(ValidationUtils.isValidPosition(null)).isFalse();
    }

    @Test
    @DisplayName("isValidPosition: debe rechazar posición vacía")
    void isValidPositionWithEmpty() {
        assertThat(ValidationUtils.isValidPosition("")).isFalse();
    }

    @Test
    @DisplayName("isValidPosition: debe rechazar posición con solo espacios")
    void isValidPositionWithBlank() {
        assertThat(ValidationUtils.isValidPosition("   ")).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Portero123", "Del@ntero", "###"})
    @DisplayName("isValidPosition: debe rechazar posiciones con caracteres especiales")
    void isValidPositionWithSpecialChars(String position) {
        assertThat(ValidationUtils.isValidPosition(position)).isFalse();
    }

    @Test
    @DisplayName("isValidPosition: debe aceptar posiciones con tildes")
    void isValidPositionWithAccents() {
        assertThat(ValidationUtils.isValidPosition("Centrocampista ofensivo")).isTrue();
    }


    @ParameterizedTest
    @ValueSource(strings = {"name", "email", "age", "stature", "dorsal", "position"})
    @DisplayName("getValidationErrorMessage: debe retornar mensaje para cada campo conocido")
    void getValidationErrorMessageWithKnownFields(String field) {
        String message = ValidationUtils.getValidationErrorMessage(field, "");
        assertThat(message).isNotBlank();
    }

    @Test
    @DisplayName("getValidationErrorMessage: debe retornar mensaje default para campo desconocido")
    void getValidationErrorMessageWithUnknownField() {
        String message = ValidationUtils.getValidationErrorMessage("unknown", "");
        assertThat(message).contains("unknown");
    }

    @Test
    @DisplayName("getValidationErrorMessage: campo 'name' debe mencionar letras y símbolos válidos")
    void getValidationErrorMessageForName() {
        String message = ValidationUtils.getValidationErrorMessage("name", "");
        assertThat(message).isNotBlank();
    }

    @Test
    @DisplayName("getValidationErrorMessage: campo 'email' debe mencionar dominios válidos")
    void getValidationErrorMessageForEmail() {
        String message = ValidationUtils.getValidationErrorMessage("email", "");
        assertThat(message).isNotBlank();
    }

    @Test
    @DisplayName("getValidationErrorMessage: campo 'age' debe mencionar rango 18-100")
    void getValidationErrorMessageForAge() {
        String message = ValidationUtils.getValidationErrorMessage("age", "");
        assertThat(message).contains("18");
        assertThat(message).contains("100");
    }

    @Test
    @DisplayName("getValidationErrorMessage: campo 'stature' debe mencionar rango cm")
    void getValidationErrorMessageForStature() {
        String message = ValidationUtils.getValidationErrorMessage("stature", "");
        assertThat(message).contains("100");
        assertThat(message).contains("300");
    }

    @Test
    @DisplayName("getValidationErrorMessage: campo 'dorsal' debe mencionar rango 0-99")
    void getValidationErrorMessageForDorsal() {
        String message = ValidationUtils.getValidationErrorMessage("dorsal", "");
        assertThat(message).contains("0");
        assertThat(message).contains("99");
    }

    @Test
    @DisplayName("getValidationErrorMessage: búsqueda insensible a mayúsculas")
    void getValidationErrorMessageCaseInsensitive() {
        String lower = ValidationUtils.getValidationErrorMessage("name", "");
        String upper = ValidationUtils.getValidationErrorMessage("NAME", "");
        assertThat(lower).isEqualTo(upper);
    }
}
