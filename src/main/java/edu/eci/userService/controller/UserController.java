package edu.eci.userService.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import edu.eci.userService.dto.LoginRequest;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
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
    public UserDTO createUser(@RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
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
}
