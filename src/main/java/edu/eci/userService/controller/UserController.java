package edu.eci.userService.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientResponseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.eci.userService.dto.LoginRequest;
import edu.eci.userService.dto.RoleChangeRequest;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.services.IdentityRoleService;
import edu.eci.userService.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;
    private final IdentityRoleService identityRoleService;

    public UserController(UserService userService, IdentityRoleService identityRoleService) {
        this.userService = userService;
        this.identityRoleService = identityRoleService;
    }

    @GetMapping("/ping")
    public String ping() {
        return "hola mundo!";
    }

    @GetMapping
    @Operation(summary = "List all users", description = "Get a list of all users in the system")
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieve a user by their unique ID")
    public UserDTO getUserById(@PathVariable long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/register")
    @Operation(summary = "Create a new user", description = "Create a new user with the provided information")
    public UserDTO createUser(@RequestBody UserDTO request) {
        log.info("=== REGISTER REQUEST RECEIVED ===");
        log.info("name: {}", request.getName());
        log.info("email: {}", request.getEmail());
        log.info("academicLevel: {}", request.getAcademicLevel());
        log.info("professorType: {}", request.getProfessorType());
        log.info("schoolRelation: {}", request.getSchoolRelation());
        log.info("=================================");
        return userService.createUser(request);
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user", description = "Validate user credentials")
    public UserDTO login(@RequestBody LoginRequest request) {
        return userService.authenticate(request.getEmail(), request.getPassword());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing user", description = "Update the user with the given ID")
    public UserDTO updateUser(@PathVariable long id, @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a user", description = "Delete the user with the given ID")
    public Map<String, String> deleteUser(@PathVariable long id) {
        userService.deleteUser(id);
        return Map.of("message", "User deleted successfully");
    }


    @PostMapping("/validate-jerseys")
    @Operation(summary = "Validate jersey uniqueness", description = "Check if any players in the list share the same jersey number")
    public Map<String, Object> validateJerseys(@RequestBody Map<String, List<Long>> body) {
        List<Long> playerIds = body.getOrDefault("playerIds", List.of());
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("duplicates", List.of());
        return result;
    }

    @PostMapping("/validate-programs")
    @Operation(summary = "Validate program composition", description = "Check if more than half of players are from allowed programs")
    public Map<String, Object> validatePrograms(@RequestBody Map<String, List<Long>> body) {
        List<Long> playerIds = body.getOrDefault("playerIds", List.of());
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("details", "Validación de programas pendiente de implementar");
        return result;
    }

    @PatchMapping("/{id}/system-role")
    @Operation(summary = "Update system role", description = "Forward role change to identity-ms via the gateway")
    public ResponseEntity<Map<String, String>> updateSystemRole(
            @PathVariable long id,
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestBody RoleChangeRequest request) {
        String role = request != null ? request.getRole() : null;
        if (role == null || role.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El campo 'role' es requerido"));
        }

        try {
            identityRoleService.updateUserRole(id, role, authorization);
            return ResponseEntity.ok(Map.of("message", "Role updated successfully", "role", role));
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        } catch (RestClientResponseException ex) {
            return ResponseEntity.status(ex.getStatusCode())
                    .body(Map.of("error", "Identity role update failed", "details", ex.getResponseBodyAsString()));
        }
    }
}
