package edu.eci.userService;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserServiceApp Tests")
class UserServiceAppTest {

    @Test
    @DisplayName("El contexto de Spring Boot arranca correctamente")
    void contextLoads() {
        assertThat(UserServiceApp.class).isNotNull();
    }

    @Test
    @DisplayName("run() delega en SpringApplication.run")
    void runDelegatesToSpringApplication() {
        ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);

        try (var springApplication = mockStatic(SpringApplication.class)) {
            springApplication.when(() -> SpringApplication.run(eq(UserServiceApp.class), eq(new String[]{})))
                    .thenReturn(context);

            ConfigurableApplicationContext result = UserServiceApp.run(new String[]{});

            assertThat(result).isSameAs(context);
        }
    }

    @Test
    @DisplayName("main() invoca run sin lanzar excepciones")
    void mainInvokesRun() {
        ConfigurableApplicationContext context = mock(ConfigurableApplicationContext.class);

        try (var springApplication = mockStatic(SpringApplication.class)) {
            springApplication.when(() -> SpringApplication.run(eq(UserServiceApp.class), eq(new String[]{})))
                    .thenReturn(context);

            UserServiceApp.main(new String[]{});
        }
    }
}
