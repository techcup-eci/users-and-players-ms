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

import edu.eci.userService.dto.AthleticProfileDTO;
import edu.eci.userService.services.AthleticProfileService;
import io.swagger.v3.oas.annotations.Operation;
@RestController
@RequestMapping("/api/athletic-profiles")
public class AthleticProfileController {

    private final AthleticProfileService athleticProfileService;

    public AthleticProfileController(AthleticProfileService athleticProfileService) {
        this.athleticProfileService = athleticProfileService;
    }

    @GetMapping
    @Operation(summary = "List all athletic profiles", description = "Get a list of all athletic profiles in the system")
    public List<AthleticProfileDTO> getAllAthleticProfiles() {
        return athleticProfileService.getAllAthleticProfiles();
    }

    @GetMapping("/{UserId}")
    @Operation(summary = "Get athletic profiles by user ID", description = "Get a list of athletic profiles that match the specified user ID")
    public AthleticProfileDTO getProfileAthleticByUserId(@PathVariable long UserId) {
        return athleticProfileService.getAthleticProfilesByUserId(UserId);
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get athletic profile by user email", description = "Get athletic profile that matches the specified user email")
    public AthleticProfileDTO getProfileAthleticByEmail(@PathVariable String email) {
        return athleticProfileService.getAthleticProfileByEmail(email);
    }

    @PostMapping
    @Operation(summary = "Create a nre athletic profile", description = "Create a new athletic profile with the provided information")
    public AthleticProfileDTO createAthleticProfile(@RequestBody AthleticProfileRequestBody requestBody) {
        return athleticProfileService.createAthleticProfile(toDTO(requestBody));
    }

    @PutMapping("/{UserId}")
    @Operation(summary = "Update an existing athletic profile", description = "Update the athletic profile")
    public AthleticProfileDTO updateAthleticProfile(@PathVariable long UserId,
            @RequestBody AthleticProfileRequestBody requestBody) {
        return athleticProfileService.updateAthleticProfile(UserId, toDTO(requestBody));
    }

    @DeleteMapping("/{UserId}")
    @Operation(summary = "Delete an athletic profile", description = "Delete the athletic profile")
    public Map<String, String> deleteAthleticProfile(@PathVariable long UserId) {
        athleticProfileService.deleteAthleticProfile(UserId);
        return Map.of("message", "User deleted successfully");
    }

    private AthleticProfileDTO toDTO(AthleticProfileRequestBody requestBody) {
        AthleticProfileDTO dto = new AthleticProfileDTO();
        dto.setDorsalNumber(requestBody.dorsalNumber());
        dto.setEmail(requestBody.email());
        dto.setPosition(requestBody.position());
        dto.setLaterality(requestBody.laterality());
        dto.setStature(requestBody.stature());
        dto.setState(requestBody.state());
        return dto;
    }

    public record AthleticProfileRequestBody(
            int dorsalNumber,
            String email,
            String position,
            String laterality,
            String stature,
            String state) {

    }
}
