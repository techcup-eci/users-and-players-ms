package edu.eci.userService.service;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.IdentificationType;
import edu.eci.userService.enums.SchoolRelation;
import edu.eci.userService.enums.UserRole;
import edu.eci.userService.enums.UserStatus;
import edu.eci.userService.mappers.UserMapper;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.services.UserService;

import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceValidationTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private UserEntity testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    public void setUp() {
        testUser = new UserEntity();
        testUser.setId(1L);
        testUser.setFullName("John Doe");
        testUser.setEmail("john@escuelaing.edu.co");
        testUser.setBirthDate(LocalDate.of(2000, 5, 15));
        testUser.setIdentificationType(IdentificationType.CC);
        testUser.setIdentificationNumber("1234567890");
        testUser.setSchoolRelation(SchoolRelation.STUDENT);
        testUser.setRole(UserRole.STUDENT);
        testUser.setStatus(UserStatus.ACTIVE);

        testUserDTO = new UserDTO();
        testUserDTO.setId(1L);
        testUserDTO.setFullName("John Doe");
        testUserDTO.setEmail("john@escuelaing.edu.co");
        testUserDTO.setBirthDate(LocalDate.of(2000, 5, 15));
        testUserDTO.setIdentificationType(IdentificationType.CC);
        testUserDTO.setIdentificationNumber("1234567890");
        testUserDTO.setSchoolRelation(SchoolRelation.STUDENT);
        testUserDTO.setRole(UserRole.STUDENT);
        testUserDTO.setStatus(UserStatus.ACTIVE);
    }

    @Test
    public void testCreateUser_InvalidName_WithNumbers() {
        testUserDTO.setFullName("John123");

        assertThatThrownBy(() -> userService.createUser(testUserDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("nombre");
    }

    @Test
    public void testCreateUser_InvalidEmail_InvalidDomain() {
        testUserDTO.setEmail("john@invalid.com");

        assertThatThrownBy(() -> userService.createUser(testUserDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("correo");
    }

    @Test
    public void testCreateUser_ValidEmail_MailEscuelaing() {
        testUserDTO.setEmail("john@mail.escuelaing.edu.co");
        when(userMapper.toEntity(testUserDTO)).thenReturn(testUser);
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.createUser(testUserDTO);
        assertThat(result).isNotNull();
    }

    @Test
    public void testCreateUser_InvalidAge_TooYoung() {
        testUserDTO.setBirthDate(LocalDate.now().minusYears(10));

        assertThatThrownBy(() -> userService.createUser(testUserDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("edad");
    }

    @Test
    public void testCreateUser_InvalidAge_TooOld() {
        testUserDTO.setBirthDate(LocalDate.now().minusYears(101));

        assertThatThrownBy(() -> userService.createUser(testUserDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("edad");
    }

    @Test
    public void testCreateUser_ValidAge() {
        testUserDTO.setBirthDate(LocalDate.of(2000, 5, 15)); // 24 años (2024)
        when(userMapper.toEntity(testUserDTO)).thenReturn(testUser);
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);
        when(userMapper.toDTO(testUser)).thenReturn(testUserDTO);

        UserDTO result = userService.createUser(testUserDTO);
        assertThat(result).isNotNull();
    }

    @Test
    public void testCreateUser_StudentMissingAcademicProgram() {
        testUserDTO.setSchoolRelation(SchoolRelation.STUDENT);
        testUserDTO.setAcademicProgram(null);

        assertThatThrownBy(() -> userService.createUser(testUserDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Academic program");
    }

    @Test
    public void testConvertToOrganizer_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        userService.convertToOrganizer(1L);

        verify(userRepository).save(argThat(user -> user.getRole() == UserRole.ORGANIZER));
    }

    @Test
    public void testConvertToOrganizer_AlreadyOrganizer() {
        testUser.setRole(UserRole.ORGANIZER);
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));

        assertThatThrownBy(() -> userService.convertToOrganizer(1L))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testDeactivateUser_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(testUser);

        userService.deactivateUser(1L);

        verify(userRepository).save(argThat(user -> user.getStatus() == UserStatus.INACTIVE));
    }
}
