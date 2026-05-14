package edu.eci.userService.controller;

import edu.eci.userService.dto.CreateSportProfileRequest;
import edu.eci.userService.dto.UpdateSportProfileRequest;
import edu.eci.userService.model.SportProfile;
import edu.eci.userService.services.SportProfileService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST controller for SportProfile management.
 * DELETE is always rejected (405) by business rule.
 */
@RestController
@RequestMapping("/api/users/{id}/sport-profile")
public class SportProfileController {

    private final SportProfileService sportProfileService;

    public SportProfileController(SportProfileService sportProfileService) {
        this.sportProfileService = sportProfileService;
    }

    @GetMapping
    public ResponseEntity<SportProfile> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(sportProfileService.getProfileByUser(id));
    }

    @PostMapping
    public ResponseEntity<SportProfile> createProfile(@PathVariable Long id,
                                                       @Valid @RequestBody CreateSportProfileRequest request) {
        SportProfile profile = sportProfileService.createProfile(
            id,
            request.getPosition(),
            request.getJerseyNumber(),
            request.getPhotoUrl()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    @PutMapping
    public ResponseEntity<SportProfile> updateProfile(@PathVariable Long id,
                                                       @Valid @RequestBody UpdateSportProfileRequest request) {
        SportProfile profile = sportProfileService.updateProfile(
            id,
            request.getPosition(),
            request.getJerseyNumber(),
            request.getPhotoUrl()
        );
        return ResponseEntity.ok(profile);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProfile(@PathVariable Long id) {
        sportProfileService.deleteProfile(id);
        return ResponseEntity.ok().build();
    }
}
