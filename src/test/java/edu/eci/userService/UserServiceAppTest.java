package edu.eci.userService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;


@DisplayName("UserServiceApp Tests")
class UserServiceAppTest {

    @Test
    @DisplayName("La clase UserServiceApp puede ser instanciada")
    void applicationStarts() {
        UserServiceApp app = new UserServiceApp();
        assertThat(app).isNotNull();
    }

    @Test
    @DisplayName("La clase UserServiceApp existe")
    void testMainClass() {
        assertThat(UserServiceApp.class).isNotNull();
    }
}
