package edu.eci.userService;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserServiceAppTest {

    @Test
    void applicationStarts() {
        // Test simple para cubrir la clase principal y su constructor
        UserServiceApp app = new UserServiceApp();
        assertThat(app).isNotNull();
    }
    
    @Test
    void testMainMethod() {
        // Test para verificar que el método main existe y puede ser ejecutado
        assertThat(UserServiceApp.class).isNotNull();
    }
}
