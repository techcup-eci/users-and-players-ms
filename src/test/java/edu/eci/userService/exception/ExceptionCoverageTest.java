package edu.eci.userService.exception;

import edu.eci.userService.exceptions.InvalidCredentialsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@DisplayName("Exception Coverage Tests")
class ExceptionCoverageTest {


    @Test
    @DisplayName("TeamNotAvailableException: constructor con mensaje")
    void teamNotAvailableExceptionWithMessage() {
        TeamNotAvailableException ex = new TeamNotAvailableException("Team not available");
        assertThat(ex.getMessage()).isEqualTo("Team not available");
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("TeamNotAvailableException: constructor con mensaje y causa")
    void teamNotAvailableExceptionWithMessageAndCause() {
        Throwable cause = new IllegalStateException("root cause");
        TeamNotAvailableException ex = new TeamNotAvailableException("Team not available", cause);
        assertThat(ex.getMessage()).isEqualTo("Team not available");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("TeamNotAvailableException: puede ser lanzada y capturada")
    void teamNotAvailableExceptionCanBeThrown() {
        assertThatThrownBy(() -> { throw new TeamNotAvailableException("thrown"); })
            .isInstanceOf(TeamNotAvailableException.class)
            .hasMessage("thrown");
    }


    @Test
    @DisplayName("UserNotFoundException: constructor con mensaje")
    void userNotFoundExceptionWithMessage() {
        UserNotFoundException ex = new UserNotFoundException("User not found");
        assertThat(ex.getMessage()).isEqualTo("User not found");
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("UserNotFoundException: constructor con mensaje y causa")
    void userNotFoundExceptionWithMessageAndCause() {
        Throwable cause = new NullPointerException("null user");
        UserNotFoundException ex = new UserNotFoundException("User not found", cause);
        assertThat(ex.getMessage()).isEqualTo("User not found");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("UserNotFoundException: puede ser lanzada y capturada")
    void userNotFoundExceptionCanBeThrown() {
        assertThatThrownBy(() -> { throw new UserNotFoundException("thrown"); })
            .isInstanceOf(UserNotFoundException.class)
            .hasMessage("thrown");
    }


    @Test
    @DisplayName("PendingJoinRequestException: constructor con mensaje")
    void pendingJoinRequestExceptionWithMessage() {
        PendingJoinRequestException ex = new PendingJoinRequestException("Already pending");
        assertThat(ex.getMessage()).isEqualTo("Already pending");
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("PendingJoinRequestException: constructor con mensaje y causa")
    void pendingJoinRequestExceptionWithMessageAndCause() {
        Throwable cause = new RuntimeException("conflict");
        PendingJoinRequestException ex = new PendingJoinRequestException("Already pending", cause);
        assertThat(ex.getMessage()).isEqualTo("Already pending");
        assertThat(ex.getCause()).isEqualTo(cause);
    }

    @Test
    @DisplayName("PendingJoinRequestException: puede ser lanzada y capturada")
    void pendingJoinRequestExceptionCanBeThrown() {
        assertThatThrownBy(() -> { throw new PendingJoinRequestException("thrown"); })
            .isInstanceOf(PendingJoinRequestException.class)
            .hasMessage("thrown");
    }


    @Test
    @DisplayName("InvalidCredentialsException: constructor sin argumentos")
    void invalidCredentialsExceptionNoArgs() {
        InvalidCredentialsException ex = new InvalidCredentialsException();
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("InvalidCredentialsException: constructor con mensaje")
    void invalidCredentialsExceptionWithMessage() {
        InvalidCredentialsException ex = new InvalidCredentialsException("Invalid credentials");
        assertThat(ex.getMessage()).isEqualTo("Invalid credentials");
        assertThat(ex).isInstanceOf(RuntimeException.class);
    }

    @Test
    @DisplayName("InvalidCredentialsException: puede ser lanzada y capturada")
    void invalidCredentialsExceptionCanBeThrown() {
        assertThatThrownBy(() -> { throw new InvalidCredentialsException("thrown"); })
            .isInstanceOf(InvalidCredentialsException.class)
            .hasMessage("thrown");
    }
}
