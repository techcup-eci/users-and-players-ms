package edu.eci.userService.dto;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class LoginRequestTest {

    @Test
    void testNoArgsConstructor() {
        LoginRequest loginRequest = new LoginRequest();

        assertThat(loginRequest).isNotNull();
        assertThat(loginRequest.getEmail()).isNull();
        assertThat(loginRequest.getPassword()).isNull();
    }

    @Test
    void testArgsConstructor() {
        String email = "test@example.com";
        String password = "myPassword123";

        LoginRequest loginRequest = new LoginRequest(email, password);

        assertThat(loginRequest.getEmail()).isEqualTo(email);
        assertThat(loginRequest.getPassword()).isEqualTo(password);
    }

    @Test
    void testSetEmail() {
        LoginRequest loginRequest = new LoginRequest();
        String email = "newEmail@example.com";

        loginRequest.setEmail(email);

        assertThat(loginRequest.getEmail()).isEqualTo(email);
    }

    @Test
    void testSetPassword() {
        LoginRequest loginRequest = new LoginRequest();
        String password = "newPassword456";

        loginRequest.setPassword(password);

        assertThat(loginRequest.getPassword()).isEqualTo(password);
    }

    @Test
    void testSetEmailAndPassword() {
        LoginRequest loginRequest = new LoginRequest();
        String email = "combined@test.com";
        String password = "combinedPassword789";

        loginRequest.setEmail(email);
        loginRequest.setPassword(password);

        assertThat(loginRequest.getEmail()).isEqualTo(email);
        assertThat(loginRequest.getPassword()).isEqualTo(password);
    }

    @Test
    void testGetEmailWithArgsConstructor() {
        String email = "getter@test.com";
        LoginRequest loginRequest = new LoginRequest(email, "pass");

        assertThat(loginRequest.getEmail()).isEqualTo(email);
    }

    @Test
    void testGetPasswordWithArgsConstructor() {
        String password = "getterPassword";
        LoginRequest loginRequest = new LoginRequest("email@test.com", password);

        assertThat(loginRequest.getPassword()).isEqualTo(password);
    }

    @Test
    void testEmailNullValue() {
        LoginRequest loginRequest = new LoginRequest();

        assertThat(loginRequest.getEmail()).isNull();

        loginRequest.setEmail("email@test.com");
        assertThat(loginRequest.getEmail()).isNotNull();

        loginRequest.setEmail(null);
        assertThat(loginRequest.getEmail()).isNull();
    }

    @Test
    void testPasswordNullValue() {
        LoginRequest loginRequest = new LoginRequest();

        assertThat(loginRequest.getPassword()).isNull();

        loginRequest.setPassword("password123");
        assertThat(loginRequest.getPassword()).isNotNull();

        loginRequest.setPassword(null);
        assertThat(loginRequest.getPassword()).isNull();
    }

    @Test
    void testEmptyStringValues() {
        LoginRequest loginRequest = new LoginRequest("", "");

        assertThat(loginRequest.getEmail()).isEmpty();
        assertThat(loginRequest.getPassword()).isEmpty();
    }

    @Test
    void testSpecialCharactersInCredentials() {
        String email = "user+test@example.co.uk";
        String password = "P@ss!w0rd#%&*";

        LoginRequest loginRequest = new LoginRequest(email, password);

        assertThat(loginRequest.getEmail()).isEqualTo(email);
        assertThat(loginRequest.getPassword()).isEqualTo(password);
    }

    @Test
    void testLongStringValues() {
        String longEmail = "a".repeat(100) + "@example.com";
        String longPassword = "p".repeat(500);

        LoginRequest loginRequest = new LoginRequest(longEmail, longPassword);

        assertThat(loginRequest.getEmail()).isEqualTo(longEmail);
        assertThat(loginRequest.getPassword()).isEqualTo(longPassword);
    }

    @Test
    void testWhitespaceInCredentials() {
        String emailWithSpaces = "  user@example.com  ";
        String passwordWithSpaces = "  pass word  ";

        LoginRequest loginRequest = new LoginRequest(emailWithSpaces, passwordWithSpaces);

        assertThat(loginRequest.getEmail()).isEqualTo(emailWithSpaces);
        assertThat(loginRequest.getPassword()).isEqualTo(passwordWithSpaces);
    }
}
