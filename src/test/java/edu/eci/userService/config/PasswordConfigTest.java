package edu.eci.userService.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class PasswordConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void testPasswordEncoderBeanExists() {
        assertThat(passwordEncoder).isNotNull();
    }

    @Test
    void testPasswordEncodingAndMatching() {
        String rawPassword = "mySecurePassword123!";
        String encodedPassword = passwordEncoder.encode(rawPassword);

        assertThat(encodedPassword).isNotEmpty();
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(passwordEncoder.matches(rawPassword, encodedPassword)).isTrue();
    }

    @Test
    void testPasswordEncodingIsConsistent() {
        String rawPassword = "testPassword456!";
        String encoded1 = passwordEncoder.encode(rawPassword);
        String encoded2 = passwordEncoder.encode(rawPassword);

        // Argon2 generates different hashes even for the same password (salting)
        assertThat(encoded1).isNotEqualTo(encoded2);
        // But both should match the original password
        assertThat(passwordEncoder.matches(rawPassword, encoded1)).isTrue();
        assertThat(passwordEncoder.matches(rawPassword, encoded2)).isTrue();
    }

    @Test
    void testIncorrectPasswordDoesNotMatch() {
        String correctPassword = "correctPassword789!";
        String wrongPassword = "wrongPassword123!";
        String encodedPassword = passwordEncoder.encode(correctPassword);

        assertThat(passwordEncoder.matches(wrongPassword, encodedPassword)).isFalse();
    }

    @Test
    void testEmptyPasswordEncoding() {
        String emptyPassword = "";
        String encodedPassword = passwordEncoder.encode(emptyPassword);

        assertThat(encodedPassword).isNotEmpty();
        assertThat(passwordEncoder.matches(emptyPassword, encodedPassword)).isTrue();
    }
}
