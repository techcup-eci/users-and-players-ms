package edu.eci.userService.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.services.UserService;
import edu.eci.userService.services.OrganizerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/users")
@Tag(name = "Users", description = "Endpoints for managing users")
public class UserController {

    private final UserService userService;
    private final OrganizerService organizerService;

    public UserController(UserService userService, OrganizerService organizerService) {
        this.userService = userService;
        this.organizerService = organizerService;
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a specific user by their ID")
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(userService.getUserById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieve all users in the system")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Create a new user in the system")
    public ResponseEntity<UserDTO> createUser(@RequestBody UserDTO userDTO) {
        try {
            UserDTO created = userService.createUser(userDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user information", description = "Update user personal data (excluding email and password)")
    public ResponseEntity<UserDTO> updateUser(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        try {
            UserDTO updated = userService.updateUser(id, userDTO);
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Deactivate user", description = "Set a user status to INACTIVE")
    public ResponseEntity<Map<String, String>> deactivateUser(@PathVariable Long id) {
        try {
            userService.deactivateUser(id);
            return ResponseEntity.ok(Map.of("message", "User deactivated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
    }

    @PatchMapping("/{playerId}/role/organizer")
    @Operation(summary = "Convert player to organizer", description = "Convert a player to organizer role (Admin only)")
    public ResponseEntity<Map<String, String>> convertToOrganizer(
            @PathVariable Long playerId,
            @RequestHeader(value = "X-Admin-Id", required = false) Long adminId) {
        try {
            // Si no se proporciona adminId en header, usar el playerId como fallback
            // En producción, el adminId vendría del JWT del token
            Long effectiveAdminId = adminId != null ? adminId : playerId;
            organizerService.convertPlayerToOrganizer(playerId, effectiveAdminId);
            return ResponseEntity.ok(Map.of("message", "Player converted to organizer successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
    }

    @GetMapping("/{id}/organizer-permissions")
    @Operation(summary = "Get organizer permissions", description = "Get the list of permissions for organizers")
    public ResponseEntity<Map<String, String>> getOrganizerPermissions(@PathVariable Long id) {
        try {
            if (!organizerService.isOrganizer(id)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("error", "User is not an organizer"));
            }
            return ResponseEntity.ok(Map.of("permissions", organizerService.getOrganizerPermissions()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found"));
        }
    }
}
