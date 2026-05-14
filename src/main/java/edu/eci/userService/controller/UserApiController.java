package edu.eci.userService.controller;

import edu.eci.userService.dto.UpdateUserRequest;
import edu.eci.userService.model.User;
import edu.eci.userService.services.UserDomainService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for User management.
 * Runs behind the API Gateway which handles authentication.
 */
@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserDomainService userService;

    public UserApiController(UserDomainService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> findById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id,
                                            @Valid @RequestBody UpdateUserRequest request) {
        User updated = userService.updateUser(
            id,
            request.getFullName(),
            request.getSchoolRelation(),
            request.getAcademicProgram(),
            request.getSemester()
        );
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<User> deactivateUser(@PathVariable Long id) {
        return ResponseEntity.ok(userService.deactivateUser(id));
    }
}
