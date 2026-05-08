package edu.eci.userService.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/AthleticProfile")
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

    @GetMapping("/{email}")
    @Operation(summary = "Get athletic profiles by email", description = "Get a list of athletic profiles that match the specified email")
    public AthleticProfileDTO getProfileAthleticByEmail(@PathVariable String email) {
        return athleticProfileService.getAthleticProfilesByEmail(email);
    }

    @PostMapping
    @Operation(summary = "Create a nre athletic profile", description = "Create a new athletic profile with the provided information")
    public AthleticProfileDTO createAthleticProfile(@RequestBody AthleticProfileRequestBody requestBody) {
        return athleticProfileService.createAthleticProfile(toDTO(requestBody));
    }

    @PutMapping("/{email}")
    @Operation(summary = "Update an existing athletic profile", description = "Update the athletic profile")
    public AthleticProfileDTO updateAthleticProfile(@PathVariable String email,
            @RequestBody AthleticProfileRequestBody requestBody) {
        return athleticProfileService.updateAthleticProfile(email, toDTO(requestBody));
    }

    @DeleteMapping("/{email}")
    @Operation(summary = "Delete an athletic profile", description = "Delete the athletic profile")
    public Map<String, String> deleteAthleticProfile(@PathVariable String email) {
        athleticProfileService.deleteAthleticProfile(email);
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
