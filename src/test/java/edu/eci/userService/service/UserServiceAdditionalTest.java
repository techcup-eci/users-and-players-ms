package edu.eci.userService.service;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.AcademicLevel;
import edu.eci.userService.enums.ProfessorType;
import edu.eci.userService.enums.SchoolRelation;
import edu.eci.userService.exceptions.InvalidCredentialsException;
import edu.eci.userService.mappers.UserMapper;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.services.UserService;


@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - casos adicionales")
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
        sampleEntity.setSchoolRelation(SchoolRelation.STUDENT);
        sampleEntity.setAcademicLevel(AcademicLevel.UNDERGRADUATE);
        sampleEntity.setProfessorType(ProfessorType.FULL_TIME);
        sampleEntity.setAcademicProgram("Ingeniería de Sistemas");
        sampleEntity.setSemester(5);
        sampleEntity.setIdentificationType("CC");
        sampleEntity.setIdentificationNumber(1000123456L);
        sampleEntity.setPhone(3001234567L);
        sampleEntity.setPassword("hashed_password");
    }


    @Nested
    @DisplayName("updateUser() - casos adicionales")
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
            updatedDTO.setSchoolRelation(SchoolRelation.PROFESSOR);
            updatedDTO.setAcademicLevel(AcademicLevel.MASTER);
            updatedDTO.setProfessorType(ProfessorType.CHAIR);
            updatedDTO.setAcademicProgram("Matemáticas");
            updatedDTO.setSemester(0);
            updatedDTO.setIdentificationType("TI");
            updatedDTO.setIdentificationNumber(9876543L);
            updatedDTO.setPhone(3109999999L);

            userService.updateUser(1L, updatedDTO);

            assertThat(sampleEntity.getName()).isEqualTo("Nombre Actualizado");
            assertThat(sampleEntity.getEmail()).isEqualTo("nuevo@gmail.com");
            assertThat(sampleEntity.getSchoolRelation()).isEqualTo(SchoolRelation.PROFESSOR);
            assertThat(sampleEntity.getSemester()).isEqualTo(0);
            verify(userRepository).save(sampleEntity);
        }
    }


    @Nested
    @DisplayName("authenticate() - casos adicionales")
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
