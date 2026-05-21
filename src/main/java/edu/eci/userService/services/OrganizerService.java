package edu.eci.userService.services;

import org.springframework.stereotype.Service;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.repository.UserRepository;

@Service
public class OrganizerService {

    private final UserRepository userRepository;

    public OrganizerService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void convertPlayerToOrganizer(Long playerId, Long adminId) {
        UserEntity admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));

        if (!"ADMIN".equals(admin.getSystemRole())) {
            throw new IllegalArgumentException("Only administrators can convert players to organizers");
        }

        UserEntity player = userRepository.findById(playerId)
                .orElseThrow(() -> new UserNotFoundException("Player not found"));

        if ("ORGANIZER".equals(player.getSystemRole())) {
            throw new IllegalArgumentException("User is already an organizer");
        }

        player.setSystemRole("ORGANIZER");
        userRepository.save(player);
    }

    public boolean isOrganizer(Long userId) {
        return userRepository.findById(userId)
                .map(user -> "ORGANIZER".equals(user.getSystemRole()))
                .orElse(false);
    }

    public boolean canOrganizerPerformAction(Long organizerId, String action) {
        UserEntity organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("Organizer not found"));

        if (!"ORGANIZER".equals(organizer.getSystemRole())) {
            return false;
        }

        return !action.equals("delete_user") && !action.equals("convert_to_organizer");
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
