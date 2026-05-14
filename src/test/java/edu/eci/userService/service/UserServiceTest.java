package edu.eci.userService.service;

import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.UserRoleEnum;
import edu.eci.userService.mappers.UserMapper;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para {@link UserService}.
 *
 * Estrategia TDD:
 *  - Cada método del servicio tiene un grupo @Nested con escenarios happy-path y de error.
 *  - Se usa Mockito para aislar el repositorio y el mapper.
 *  - AssertJ para aserciones expresivas.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    // ── Fixtures ─────────────────────────────────────────────────────────────

    private UserEntity sampleEntity;
    private UserDTO sampleDTO;

    @BeforeEach
    void setUp() {
        sampleEntity = new UserEntity();
        sampleEntity.setId(1L);
        sampleEntity.setName("Juan Pérez");
        sampleEntity.setEmail("juan.perez@eci.edu.co");
        sampleEntity.setBirthDate(LocalDate.of(2000, 5, 15));
        sampleEntity.setRole(UserRoleEnum.STUDENT);
        sampleEntity.setRelationShip("student");
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
        sampleDTO.setRole(UserRoleEnum.STUDENT);
        sampleDTO.setRelationShip("student");
        sampleDTO.setAcademicProgram("Ingeniería de Sistemas");
        sampleDTO.setSemester(5);
        sampleDTO.setIdentificationType("CC");
        sampleDTO.setIdentificationNumber(1000123456L);
        sampleDTO.setPhone(3001234567L);
        sampleDTO.setPassword("hashed_password");
    }

    // ── getAllUsers ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getAllUsers()")
    class GetAllUsers {

        @Test
        @DisplayName("Debe retornar lista de DTOs cuando existen usuarios")
        void shouldReturnDTOList() {
            when(userRepository.findAll()).thenReturn(List.of(sampleEntity));
            when(userMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

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

    // ── getUserById ──────────────────────────────────────────────────────────

    @Nested
    @DisplayName("getUserById()")
    class GetUserById {

        @Test
        @DisplayName("Debe retornar DTO cuando el usuario existe")
        void shouldReturnDTOWhenFound() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(userMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

            UserDTO result = userService.getUserById(1L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getName()).isEqualTo("Juan Pérez");
        }

        @Test
        @DisplayName("Debe retornar null cuando el usuario no existe")
        void shouldReturnNullWhenNotFound() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            when(userMapper.toDTO(null)).thenReturn(null);

            UserDTO result = userService.getUserById(99L);

            assertThat(result).isNull();
        }
    }

    // ── createUser ───────────────────────────────────────────────────────────

    @Nested
    @DisplayName("createUser()")
    class CreateUser {

        @Test
        @DisplayName("Debe persistir y retornar el DTO del usuario creado")
        void shouldPersistAndReturnDTO() {
            when(userMapper.toEntity(sampleDTO)).thenReturn(sampleEntity);
            when(userRepository.save(sampleEntity)).thenReturn(sampleEntity);
            when(userMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

            UserDTO result = userService.createUser(sampleDTO);

            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("juan.perez@eci.edu.co");
            verify(userRepository).save(sampleEntity);
        }

        @Test
        @DisplayName("Debe mapear correctamente todos los campos al crear")
        void shouldMapAllFieldsOnCreate() {
            when(userMapper.toEntity(sampleDTO)).thenReturn(sampleEntity);
            when(userRepository.save(any())).thenReturn(sampleEntity);
            when(userMapper.toDTO(sampleEntity)).thenReturn(sampleDTO);

            UserDTO result = userService.createUser(sampleDTO);

            assertThat(result.getRole()).isEqualTo(UserRoleEnum.STUDENT);
            assertThat(result.getSemester()).isEqualTo(5);
            assertThat(result.getAcademicProgram()).isEqualTo("Ingeniería de Sistemas");
        }
    }

    // ── updateUser ───────────────────────────────────────────────────────────

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
            updatedDTO.setRole(UserRoleEnum.STUDENT);
            updatedDTO.setRelationShip("student");
            updatedDTO.setAcademicProgram("Ingeniería de IA");
            updatedDTO.setSemester(6);
            updatedDTO.setIdentificationType("CC");
            updatedDTO.setIdentificationNumber(1000123456L);
            updatedDTO.setPhone(3009999999L);

            UserEntity updatedEntity = new UserEntity();
            updatedEntity.setId(1L);
            updatedEntity.setName("Juan Actualizado");

            UserDTO updatedResultDTO = new UserDTO();
            updatedResultDTO.setId(1L);
            updatedResultDTO.setName("Juan Actualizado");

            when(userRepository.findById(1L)).thenReturn(Optional.of(sampleEntity));
            when(userRepository.save(sampleEntity)).thenReturn(sampleEntity);
            when(userMapper.toDTO(sampleEntity)).thenReturn(updatedResultDTO);

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
    }

    // ── deleteUser ───────────────────────────────────────────────────────────

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
}
