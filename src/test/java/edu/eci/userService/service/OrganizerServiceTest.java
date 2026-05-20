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

import edu.eci.userService.entities.UserEntity;

import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.services.OrganizerService;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class OrganizerServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OrganizerService organizerService;

    private UserEntity adminUser;
    private UserEntity playerUser;

    @BeforeEach
    public void setUp() {
        adminUser = new UserEntity();
        adminUser.setId(1L);
        adminUser.setName("Admin User");
        adminUser.setSystemRole("ADMIN");

        playerUser = new UserEntity();
        playerUser.setId(2L);
        playerUser.setName("Player User");
        playerUser.setSystemRole("PLAYER");
    }

    @Test
    public void testConvertPlayerToOrganizer_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(playerUser));
        when(userRepository.save(any(UserEntity.class))).thenReturn(playerUser);

        organizerService.convertPlayerToOrganizer(2L, 1L);

        verify(userRepository).save(argThat(user -> "ORGANIZER".equals(user.getSystemRole())));
    }

    @Test
    public void testConvertPlayerToOrganizer_AdminNotFound() {
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> organizerService.convertPlayerToOrganizer(2L, 99L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testConvertPlayerToOrganizer_PlayerNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> organizerService.convertPlayerToOrganizer(99L, 1L))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    public void testConvertPlayerToOrganizer_OnlyAdminCanConvert() {
        UserEntity nonAdmin = new UserEntity();
        nonAdmin.setId(3L);
        nonAdmin.setSystemRole("PLAYER");

        when(userRepository.findById(3L)).thenReturn(Optional.of(nonAdmin));

        assertThatThrownBy(() -> organizerService.convertPlayerToOrganizer(2L, 3L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Only administrators");
    }

    @Test
    public void testConvertPlayerToOrganizer_AlreadyOrganizer() {
        playerUser.setSystemRole("ORGANIZER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));
        when(userRepository.findById(2L)).thenReturn(Optional.of(playerUser));

        assertThatThrownBy(() -> organizerService.convertPlayerToOrganizer(2L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already an organizer");
    }

    @Test
    public void testIsOrganizer_True() {
        UserEntity organizer = new UserEntity();
        organizer.setId(1L);
        organizer.setSystemRole("ORGANIZER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        boolean result = organizerService.isOrganizer(1L);

        assertThat(result).isTrue();
    }

    @Test
    public void testIsOrganizer_False() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(playerUser));

        boolean result = organizerService.isOrganizer(1L);

        assertThat(result).isFalse();
    }

    @Test
    public void testCanOrganizerPerformAction_AllowedAction() {
        UserEntity organizer = new UserEntity();
        organizer.setId(1L);
        organizer.setSystemRole("ORGANIZER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        boolean result = organizerService.canOrganizerPerformAction(1L, "create_team");

        assertThat(result).isTrue();
    }

    @Test
    public void testCanOrganizerPerformAction_DeleteUserNotAllowed() {
        UserEntity organizer = new UserEntity();
        organizer.setId(1L);
        organizer.setSystemRole("ORGANIZER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        boolean result = organizerService.canOrganizerPerformAction(1L, "delete_user");

        assertThat(result).isFalse();
    }

    @Test
    public void testCanOrganizerPerformAction_ConvertToOrganizerNotAllowed() {
        UserEntity organizer = new UserEntity();
        organizer.setId(1L);
        organizer.setSystemRole("ORGANIZER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(organizer));

        boolean result = organizerService.canOrganizerPerformAction(1L, "convert_to_organizer");

        assertThat(result).isFalse();
    }

    @Test
    public void testGetOrganizerPermissions() {
        String permissions = organizerService.getOrganizerPermissions();

        assertThat(permissions).contains("View users");
        assertThat(permissions).contains("Create teams");
        assertThat(permissions).contains("EXCLUDED: Delete users");
    }
}
