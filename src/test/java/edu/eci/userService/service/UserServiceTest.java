package edu.eci.userService.service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
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
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.dto.UserRegisterRequest;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.UserRole;
import edu.eci.userService.exceptions.InvalidCredentialsException;
import edu.eci.userService.mappers.UserMapper;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.services.UserService;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    private final UserMapper userMapper = new UserMapper();

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    private UserEntity sampleEntity;
    private UserDTO sampleDTO;
    private UserRegisterRequest sampleRegisterRequest;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository, userMapper, passwordEncoder, "STUDENT");
        sampleEntity = new UserEntity();
        sampleEntity.setId(1L);
        sampleEntity.setName("Juan Pérez");
        sampleEntity.setEmail("juan.perez@eci.edu.co");
        sampleEntity.setBirthDate(LocalDate.of(2000, 5, 15));
        sampleEntity.setRole(UserRole.STUDENT);
        sampleEntity.setRelationship("student");
        sampleEntity.setAcademicProgram("Ingeniería de Sistemas");
        sampleEntity.setSemester(5);
        sampleEntity.setIdentificationType("CC");
        sampleEntity.setIdentificationNumber(1000123456L);
        sampleEntity.setPhone(3001234567L);
        sampleEntity.setPassword("hashed_password");

        sampleDTO = new UserDTO();
        sampleDTO.setId(1L);
        sampleDTO.setName("Juan Pérez");
        sampleDTO.setEmail("juan.perez@eci.edu.co");
        sampleDTO.setBirthDate(LocalDate.of(2000, 5, 15));
        sampleDTO.setRole(UserRole.STUDENT);
        sampleDTO.setRelationship("student");
        sampleDTO.setAcademicProgram("Ingeniería de Sistemas");
        sampleDTO.setSemester(5);
        sampleDTO.setIdentificationType("CC");
        sampleDTO.setIdentificationNumber(1000123456L);
        sampleDTO.setPhone(3001234567L);
        sampleDTO.setPassword("plain_password");

        sampleRegisterRequest = new UserRegisterRequest();
        sampleRegisterRequest.setName("Juan Pérez");
        sampleRegisterRequest.setEmail("juan.perez@eci.edu.co");
        sampleRegisterRequest.setBirthDate(LocalDate.of(2000, 5, 15));
        sampleRegisterRequest.setRelationship("student");
        sampleRegisterRequest.setAcademicProgram("Ingeniería de Sistemas");
        sampleRegisterRequest.setSemester(5);
        sampleRegisterRequest.setIdentificationType("CC");
        sampleRegisterRequest.setIdentificationNumber(1000123456L);
        sampleRegisterRequest.setPhone(3001234567L);
        sampleRegisterRequest.setPassword("plain_password");

        lenient().when(passwordEncoder.encode("plain_password")).thenReturn("hashed_password");
    }


    @Nested
    @DisplayName("getAllUsers()")
    class GetAllUsers {

        @Test
        @DisplayName("Debe retornar lista de DTOs cuando existen usuarios")
        void shouldReturnDTOList() {
            when(userRepository.findAll()).thenReturn(List.of(sampleEntity));

            List<UserDTO> result = userService.getAllUsers();

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getEmail()).isEqualTo("juan.perez@eci.edu.co");
            verify(userRepository).findAll();
        }

        @Test
        @DisplayName("Debe retornar lista vacía cuando no hay usuarios")
        void shouldReturnEmptyListWhenNoUsers() {
            when(userRepository.findAll()).thenReturn(List.of());

            List<UserDTO> result = userService.getAllUsers();

            assertThat(result).isEmpty();
        }
    }


    @Nested
    @DisplayName("getUserById()")
    class GetUserById {

        @Test
        @DisplayName("Debe retornar DTO cuando el usuario existe")
        void shouldReturnDTOWhenFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));

            UserDTO result = userService.getUserById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Juan Pérez");
        }

        @Test
        @DisplayName("Debe retornar null cuando el usuario no existe")
        void shouldReturnNullWhenNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            UserDTO result = userService.getUserById(99L);

            assertThat(result).isNull();
        }
    }


    @Nested
    @DisplayName("createUser()")
    class CreateUser {

        @Test
        @DisplayName("Debe persistir y retornar el DTO del usuario creado")
        void shouldPersistAndReturnDTO() {
            when(userRepository.save(any(UserEntity.class))).thenReturn(sampleEntity);

            UserDTO result = userService.createUser(sampleRegisterRequest);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("juan.perez@eci.edu.co");
            verify(userRepository).save(any(UserEntity.class));
        }

        @Test
        @DisplayName("Debe mapear correctamente todos los campos al crear")
        void shouldMapAllFieldsOnCreate() {
            when(userRepository.save(any(UserEntity.class))).thenReturn(sampleEntity);

            UserDTO result = userService.createUser(sampleRegisterRequest);

            assertThat(result.getRole()).isEqualTo(UserRole.STUDENT);
            assertThat(result.getSemester()).isEqualTo(5);
            assertThat(result.getAcademicProgram()).isEqualTo("Ingeniería de Sistemas");
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException cuando la contraseña es null")
        void shouldThrowWhenPasswordIsNull() {
            sampleRegisterRequest.setPassword(null);

            assertThatThrownBy(() -> userService.createUser(sampleRegisterRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Password");
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException cuando la contraseña esta vacia")
        void shouldThrowWhenPasswordIsBlank() {
            sampleRegisterRequest.setPassword(" ");

            assertThatThrownBy(() -> userService.createUser(sampleRegisterRequest))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("Password");
        }
    }


    @Nested
    @DisplayName("updateUser()")
    class UpdateUser {

        @Test
        @DisplayName("Debe actualizar y retornar DTO cuando el usuario existe")
        void shouldUpdateAndReturnDTO() {
            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setName("Juan Actualizado");
            updatedDTO.setEmail("juan.perez@eci.edu.co");
            updatedDTO.setBirthDate(LocalDate.of(2000, 5, 15));
            updatedDTO.setRole(UserRole.STUDENT);
            updatedDTO.setRelationship("student");
            updatedDTO.setAcademicProgram("Ingeniería de IA");
            updatedDTO.setSemester(6);
            updatedDTO.setIdentificationType("CC");
            updatedDTO.setIdentificationNumber(1000123456L);
            updatedDTO.setPhone(3009999999L);

            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(userRepository.save(sampleEntity)).thenReturn(sampleEntity);

            UserDTO result = userService.updateUser(1L, updatedDTO);

            assertThat(result.getName()).isEqualTo("Juan Actualizado");
            verify(userRepository).save(sampleEntity);
        }

        @Test
        @DisplayName("Debe lanzar NoSuchElementException cuando el usuario no existe")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> userService.updateUser(99L, sampleDTO))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("99");
        }

        @Test
        @DisplayName("Debe actualizar el password cuando se envia uno nuevo")
        void shouldUpdatePasswordWhenProvided() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(passwordEncoder.encode("new_password")).thenReturn("hashed_new_password");
            when(userRepository.save(sampleEntity)).thenReturn(sampleEntity);

            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setPassword("new_password");

            userService.updateUser(1L, updatedDTO);

            verify(passwordEncoder).encode("new_password");
            assertThat(sampleEntity.getPassword()).isEqualTo("hashed_new_password");
        }

        @Test
        @DisplayName("No debe actualizar el password cuando esta vacio")
        void shouldNotUpdatePasswordWhenBlank() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(userRepository.save(sampleEntity)).thenReturn(sampleEntity);

            UserDTO updatedDTO = new UserDTO();
            updatedDTO.setPassword(" ");

            userService.updateUser(1L, updatedDTO);

            verify(passwordEncoder, never()).encode(any());
        }
    }


    @Nested
    @DisplayName("deleteUser()")
    class DeleteUser {

        @Test
        @DisplayName("Debe eliminar el usuario cuando existe")
        void shouldDeleteWhenExists() {
            when(userRepository.existsById(1L)).thenReturn(true);

            userService.deleteUser(1L);

            verify(userRepository).deleteById(1L);
        }

        @Test
        @DisplayName("Debe lanzar NoSuchElementException cuando el usuario no existe")
        void shouldThrowWhenNotFound() {
            when(userRepository.existsById(99L)).thenReturn(false);

            assertThatThrownBy(() -> userService.deleteUser(99L))
                    .isInstanceOf(NoSuchElementException.class)
                    .hasMessageContaining("99");

            verify(userRepository, never()).deleteById(any());
        }
    }


    @Nested
    @DisplayName("authenticate()")
    class Authenticate {

        @Test
        @DisplayName("Debe autenticar cuando las credenciales son correctas")
        void shouldAuthenticateWhenCredentialsAreValid() {
            when(userRepository.findByEmail("juan.perez@eci.edu.co")).thenReturn(sampleEntity);
            when(passwordEncoder.matches("plain_password", "hashed_password")).thenReturn(true);

            UserDTO result = userService.authenticate("juan.perez@eci.edu.co", "plain_password");

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("juan.perez@eci.edu.co");
        }

        @Test
        @DisplayName("Debe lanzar InvalidCredentialsException cuando la clave es incorrecta")
        void shouldThrowWhenPasswordIsInvalid() {
            when(userRepository.findByEmail("juan.perez@eci.edu.co")).thenReturn(sampleEntity);
            when(passwordEncoder.matches("wrong_password", "hashed_password")).thenReturn(false);

            assertThatThrownBy(() -> userService.authenticate("juan.perez@eci.edu.co", "wrong_password"))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        @DisplayName("Debe lanzar InvalidCredentialsException cuando el usuario no existe")
        void shouldThrowWhenUserNotFound() {
            when(userRepository.findByEmail("missing@eci.edu.co")).thenReturn(null);

            assertThatThrownBy(() -> userService.authenticate("missing@eci.edu.co", "plain_password"))
                    .isInstanceOf(InvalidCredentialsException.class);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException cuando faltan datos")
        void shouldThrowWhenMissingData() {
            assertThatThrownBy(() -> userService.authenticate("", "plain_password"))
                    .isInstanceOf(IllegalArgumentException.class);

            assertThatThrownBy(() -> userService.authenticate("juan.perez@eci.edu.co", ""))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException cuando email es null")
        void shouldThrowWhenEmailIsNull() {
            assertThatThrownBy(() -> userService.authenticate(null, "plain_password"))
                .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        @DisplayName("Debe lanzar IllegalArgumentException cuando password es null")
        void shouldThrowWhenPasswordIsNull() {
            assertThatThrownBy(() -> userService.authenticate("juan.perez@eci.edu.co", null))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }
}
