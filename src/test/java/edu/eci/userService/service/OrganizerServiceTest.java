package edu.eci.userService.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import edu.eci.userService.services.IdentityRoleService;
import edu.eci.userService.services.OrganizerService;

@ExtendWith(MockitoExtension.class)
public class OrganizerServiceTest {

    @Mock
    private IdentityRoleService identityRoleService;

    @InjectMocks
    private OrganizerService organizerService;

    @Test
    public void testConvertPlayerToOrganizer_Success() {
        organizerService.convertPlayerToOrganizer(2L, "Bearer token");

        verify(identityRoleService).updateUserRole(2L, "ORGANIZER", "Bearer token");
    }

    @Test
    public void testIsOrganizer_True() {
        boolean result = organizerService.isOrganizer("ORGANIZER");
        assertThat(result).isTrue();
    }

    @Test
    public void testIsOrganizer_False() {
        boolean result = organizerService.isOrganizer("PLAYER");
        assertThat(result).isFalse();
    }

    @Test
    public void testCanOrganizerPerformAction_AllowedAction() {
        boolean result = organizerService.canOrganizerPerformAction("ORGANIZER", "create_team");
        assertThat(result).isTrue();
    }

    @Test
    public void testCanOrganizerPerformAction_DeleteUserNotAllowed() {
        boolean result = organizerService.canOrganizerPerformAction("ORGANIZER", "delete_user");
        assertThat(result).isFalse();
    }

    @Test
    public void testCanOrganizerPerformAction_ConvertToOrganizerNotAllowed() {
        boolean result = organizerService.canOrganizerPerformAction("ORGANIZER", "convert_to_organizer");
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
