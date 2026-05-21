package edu.eci.userService.service;

import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.UserRoleEnum;
import edu.eci.userService.mappers.UserMapper;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.services.UserService;
import edu.eci.userService.exceptions.InvalidCredentialsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Additional Branch Coverage Tests")
class UserServiceAdditionalTest {

    @Mock
    private UserRepository userRepository;

    private final UserMapper userMapper = new UserMapper();

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;
    private UserEntity sampleEntity;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userMapper, passwordEncoder);

        sampleEntity = new UserEntity();
        sampleEntity.setId(1L);
        sampleEntity.setName("Juan Pérez");
        sampleEntity.setEmail("juan@gmail.com");
        sampleEntity.setBirthDate(LocalDate.of(2000, 5, 15));
        sampleEntity.setRole(UserRoleEnum.STUDENT);
        sampleEntity.setRelationship("student");
        sampleEntity.setAcademicProgram("Ingeniería de Sistemas");
        sampleEntity.setSemester(5);
        sampleEntity.setIdentificationType("CC");
        sampleEntity.setIdentificationNumber(1000123456L);
        sampleEntity.setPhone(3001234567L);
        sampleEntity.setSystemRole("PLAYER");
        sampleEntity.setPassword("hashed_password");
    }

    // ── updateUser — ramas adicionales ──────────────────────────────────────

    @Nested
    @DisplayName("updateUser() — branch coverage adicional")
    class UpdateUserBranches {

        @Test
        @DisplayName("Debe preservar password cuando el nuevo password es null")
        void shouldNotUpdatePasswordWhenNull() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(userRepository.save(sampleEntity)).thenReturn(sampleEntity);

            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setName("Nuevo Nombre");
            updatedDTO.setPassword(null);

            userService.updateUser(1L, updatedDTO);

            verify(passwordEncoder, never()).encode(any());
        }

        @Test
        @DisplayName("Debe actualizar todos los campos de updateUser correctamente")
        void shouldUpdateAllFieldsCorrectly() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(userRepository.save(sampleEntity)).thenReturn(sampleEntity);

            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setName("Nombre Actualizado");
            updatedDTO.setEmail("nuevo@gmail.com");
            updatedDTO.setBirthDate(LocalDate.of(1999, 1, 1));
            updatedDTO.setRole(UserRoleEnum.TEACHER);
            updatedDTO.setRelationship("teacher");
            updatedDTO.setAcademicProgram("Matemáticas");
            updatedDTO.setSemester(0);
            updatedDTO.setIdentificationType("TI");
            updatedDTO.setIdentificationNumber(9876543L);
            updatedDTO.setPhone(3109999999L);

            userService.updateUser(1L, updatedDTO);

            assertThat(sampleEntity.getName()).isEqualTo("Nombre Actualizado");
            assertThat(sampleEntity.getEmail()).isEqualTo("nuevo@gmail.com");
            assertThat(sampleEntity.getRole()).isEqualTo(UserRoleEnum.TEACHER);
            assertThat(sampleEntity.getSemester()).isEqualTo(0);
            verify(userRepository).save(sampleEntity);
        }
    }

    // ── updateSystemRole ─────────────────────────────────────────────────────

    @Nested
    @DisplayName("updateSystemRole()")
    class UpdateSystemRole {

        @Test
        @DisplayName("Debe actualizar el systemRole cuando el usuario existe")
        void shouldUpdateSystemRoleWhenUserExists() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(userRepository.save(sampleEntity)).thenReturn(sampleEntity);

            userService.updateSystemRole(1L, "CAPTAIN");

            assertThat(sampleEntity.getSystemRole()).isEqualTo("CAPTAIN");
            verify(userRepository).save(sampleEntity);
        }

        @Test
        @DisplayName("Debe lanzar NoSuchElementException cuando el usuario no existe")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.updateSystemRole(99L, "CAPTAIN"))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("99");
        }
    }

    // ── authenticate — ramas adicionales ────────────────────────────────────

    @Nested
    @DisplayName("authenticate() — branch coverage adicional")
    class AuthenticateBranches {

        @Test
        @DisplayName("Debe lanzar InvalidCredentialsException cuando la entidad existe pero password no coincide")
        void shouldThrowWhenPasswordDoesNotMatch() {
            when(userRepository.findByEmail("juan@gmail.com")).thenReturn(sampleEntity);
            when(passwordEncoder.matches("wrong", "hashed_password")).thenReturn(false);

            assertThatThrownBy(() -> userService.authenticate("juan@gmail.com", "wrong"))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        @DisplayName("Debe lanzar InvalidCredentialsException cuando el usuario es null (no encontrado)")
        void shouldThrowWhenUserIsNull() {
            when(userRepository.findByEmail("noexiste@gmail.com")).thenReturn(null);

            assertThatThrownBy(() -> userService.authenticate("noexiste@gmail.com", "password"))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException cuando email tiene solo espacios")
        void shouldThrowWhenEmailIsBlankSpaces() {
            assertThatThrownBy(() -> userService.authenticate("   ", "password"))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException cuando password tiene solo espacios")
        void shouldThrowWhenPasswordIsBlankSpaces() {
            assertThatThrownBy(() -> userService.authenticate("juan@gmail.com", "   "))
                    .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ── getUserById — rama con null ──────────────────────────────────────────

    @Nested
    @DisplayName("getUserById() — rama null")
    class GetUserByIdNull {

        @Test
        @DisplayName("Debe retornar null cuando el repositorio no encuentra el usuario")
        void shouldReturnNullWhenNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            UserDTO result = userService.getUserById(99L);

            assertThat(result).isNull();
        }
    }
}
