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

    /**
     * Convierte un jugador (PLAYER role) a organizador.
     * Solo admins pueden hacer esto.
     */
    public void convertPlayerToOrganizer(Long playerId, Long adminId) {
        // Verificar que quien lo solicita es admin
        UserEntity admin = userRepository.findById(adminId)
                .orElseThrow(() -> new UserNotFoundException("Admin not found"));

        if (!"ADMIN".equals(admin.getSystemRole())) {
            throw new IllegalArgumentException("Only administrators can convert players to organizers");
        }

        // Obtener el jugador a convertir
        UserEntity player = userRepository.findById(playerId)
                .orElseThrow(() -> new UserNotFoundException("Player not found"));

        // Verificar que no sea ya un organizador
        if ("ORGANIZER".equals(player.getSystemRole())) {
            throw new IllegalArgumentException("User is already an organizer");
        }

        // Convertir el rol
        player.setSystemRole("ORGANIZER");
        userRepository.save(player);
    }

    /**
     * Verifica si un usuario tiene permisos de organizador
     */
    public boolean isOrganizer(Long userId) {
        return userRepository.findById(userId)
                .map(user -> "ORGANIZER".equals(user.getSystemRole()))
                .orElse(false);
    }

    /**
     * Verifica si un organizador puede realizar una acción específica.
     * Organizador = Admin permissions - (delete users + convert players to
     * organizer)
     */
    public boolean canOrganizerPerformAction(Long organizerId, String action) {
        UserEntity organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException("Organizer not found"));

        if (!"ORGANIZER".equals(organizer.getSystemRole())) {
            return false;
        }

        // Acciones que NO puede hacer un organizador
        return !action.equals("delete_user") && !action.equals("convert_to_organizer");
    }

    /**
     * Obtiene la descripción de permisos de un organizador
     */
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
