package edu.eci.userService.services;

import org.springframework.stereotype.Service;
import java.util.Objects;

@Service
public class OrganizerService {

    private final IdentityRoleService identityRoleService;

    public OrganizerService(IdentityRoleService identityRoleService) {
        this.identityRoleService = identityRoleService;
    }

    public void convertPlayerToOrganizer(Long playerId, String authorization) {
        identityRoleService.updateUserRole(playerId, "ORGANIZER", authorization);
    }

    public boolean isOrganizer(String systemRole) {
        return Objects.equals("ORGANIZER", systemRole);
    }

    public boolean canOrganizerPerformAction(String systemRole, String action) {
        if (!Objects.equals("ORGANIZER", systemRole)) {
            return false;
        }
        return !"delete_user".equals(action) && !"convert_to_organizer".equals(action);
    }

    public String getOrganizerPermissions() {
        return "Organizador permissions: "
                + "- View users (all) "
                + "- Create teams "
                + "- Manage tournaments "
                + "- Invite players "
                + "- Manage team players "
                + "- EXCLUDED: Delete users, Convert players to organizers";
    }
}
