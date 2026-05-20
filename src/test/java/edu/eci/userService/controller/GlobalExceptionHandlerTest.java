package edu.eci.userService.controller;

import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import edu.eci.userService.exceptions.InvalidCredentialsException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleNotFound() {
        String errorMessage = "Resource not found";
        NoSuchElementException exception = new NoSuchElementException(errorMessage);

        ResponseEntity<Map<String, String>> response = handler.handleNotFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo(errorMessage);
    }

    @Test
    void testHandleNotFoundWithEmptyMessage() {
        NoSuchElementException exception = new NoSuchElementException();

        ResponseEntity<Map<String, String>> response = handler.handleNotFound(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().containsKey("error")).isTrue();
    }

    @Test
    void testHandleBadRequest() {
        String errorMessage = "Invalid input provided";
        IllegalArgumentException exception = new IllegalArgumentException(errorMessage);

        ResponseEntity<Map<String, String>> response = handler.handleBadRequest(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo(errorMessage);
    }

    @Test
    void testHandleBadRequestWithEmptyMessage() {
        IllegalArgumentException exception = new IllegalArgumentException();

        ResponseEntity<Map<String, String>> response = handler.handleBadRequest(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().containsKey("error")).isTrue();
    }

    @Test
    void testHandleInvalidCredentials() {
        String errorMessage = "Invalid email or password";
        InvalidCredentialsException exception = new InvalidCredentialsException(errorMessage);

        ResponseEntity<Map<String, String>> response = handler.handleInvalidCredentials(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().get("error")).isEqualTo(errorMessage);
    }

    @Test
    void testHandleInvalidCredentialsWithEmptyMessage() {
        InvalidCredentialsException exception = new InvalidCredentialsException();

        ResponseEntity<Map<String, String>> response = handler.handleInvalidCredentials(exception);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().containsKey("error")).isTrue();
    }

    @Test
    void testHandleNotFoundMultipleCalls() {
        NoSuchElementException exception1 = new NoSuchElementException("Error 1");
        NoSuchElementException exception2 = new NoSuchElementException("Error 2");

        ResponseEntity<Map<String, String>> response1 = handler.handleNotFound(exception1);
        ResponseEntity<Map<String, String>> response2 = handler.handleNotFound(exception2);

        assertThat(response1.getBody().get("error")).isEqualTo("Error 1");
        assertThat(response2.getBody().get("error")).isEqualTo("Error 2");
    }

    @Test
    void testResponseBodyStructure() {
        ResponseEntity<Map<String, String>> response = handler.handleBadRequest(new IllegalArgumentException("Test"));

        assertThat(response.getBody()).containsOnlyKeys("error");
        assertThat(response.getBody().size()).isEqualTo(1);
    }
}
