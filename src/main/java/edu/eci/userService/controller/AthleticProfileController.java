package edu.eci.userService.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import edu.eci.userService.enums.LateralityType;
import edu.eci.userService.enums.ProfileStatus;
import edu.eci.userService.services.AthleticProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/players/profile")
@Tag(name = "Athletic Profile", description = "Endpoints for managing player athletic profiles")
public class AthleticProfileController {

    private final AthleticProfileService athleticProfileService;

    public AthleticProfileController(AthleticProfileService athleticProfileService) {
        this.athleticProfileService = athleticProfileService;
    }

    @GetMapping
    @Operation(summary = "List all athletic profiles", description = "Get a list of all athletic profiles in the system")
    public ResponseEntity<List<AthleticProfileDTO>> getAllAthleticProfiles() {
        return ResponseEntity.ok(athleticProfileService.getAllAthleticProfiles());
    }

    @GetMapping("/{userId}")
    @Operation(summary = "Get athletic profile by user ID", description = "Get the athletic profile for a specific user")
    public ResponseEntity<AthleticProfileDTO> getProfileByUserId(@PathVariable Long userId) {
        try {
            return ResponseEntity.ok(athleticProfileService.getAthleticProfileByUserId(userId));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PostMapping
    @Operation(summary = "Create a new athletic profile", description = "Create a new athletic profile for a user")
    public ResponseEntity<AthleticProfileDTO> createAthleticProfile(
            @RequestBody AthleticProfileRequestBody requestBody) {
        try {
            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setDorsalNumber(requestBody.dorsalNumber());
            dto.setPosition(requestBody.position());
            dto.setLaterality(LateralityType.valueOf(requestBody.laterality()));
            dto.setStature(requestBody.stature());
            dto.setStatus(ProfileStatus.valueOf(requestBody.status()));

            AthleticProfileDTO result = athleticProfileService.createAthleticProfile(
                    requestBody.userId(), dto);
            return ResponseEntity.status(HttpStatus.CREATED).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update an existing athletic profile", description = "Update the athletic profile for a user")
    public ResponseEntity<AthleticProfileDTO> updateAthleticProfile(@PathVariable Long userId,
            @RequestBody AthleticProfileRequestBody requestBody) {
        try {
            AthleticProfileDTO dto = new AthleticProfileDTO();
            dto.setDorsalNumber(requestBody.dorsalNumber());
            dto.setPosition(requestBody.position());
            dto.setLaterality(LateralityType.valueOf(requestBody.laterality()));
            dto.setStature(requestBody.stature());
            dto.setStatus(ProfileStatus.valueOf(requestBody.status()));
            dto.setPhotoUrl(requestBody.photoUrl());

            AthleticProfileDTO result = athleticProfileService.updateAthleticProfile(userId, dto);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete an athletic profile", description = "Attempt to delete an athletic profile (operation not permitted)")
    public ResponseEntity<Map<String, String>> deleteAthleticProfile(@PathVariable Long userId) {
        try {
            athleticProfileService.deleteAthleticProfile(userId);
            return ResponseEntity.ok(Map.of("message", "Deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body(Map.of("error",
                            "Athletic profile deletion is not permitted to preserve tournament historical integrity"));
        }
    }

    public record AthleticProfileRequestBody(
            Long userId,
            Integer dorsalNumber,
            String position,
            String laterality,
            Integer stature,
            String status,
            String photoUrl) {
    }
}
