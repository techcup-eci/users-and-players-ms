package edu.eci.userService.services;

import edu.eci.userService.exception.DeleteSportProfileNotAllowedException;
import edu.eci.userService.exception.PlayerAlreadyAssignedToTeamException;
import edu.eci.userService.exception.SportProfileAlreadyExistsException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.model.SportProfile;
import edu.eci.userService.model.enums.PlayingPosition;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Service for SportProfile management.
 * Implements business rules defined in TECHCUP FOOTBALL document section 7.2.
 *
 * Note: In-memory store used as placeholder until a JPA entity/repository
 * for SportProfile is created.
 */
@Service
public class SportProfileService {

    // In-memory store keyed by userId — to be replaced with JPA repository
    private final Map<Long, SportProfile> profileStore = new HashMap<>();
    private long nextId = 1L;

    public SportProfile getProfileByUser(Long userId) {
        SportProfile profile = profileStore.get(userId);
        if (profile == null) {
            throw new UserNotFoundException("Sport profile not found for user: " + userId);
        }
        return profile;
    }

    public SportProfile createProfile(Long userId, PlayingPosition position,
                                      int jerseyNumber, String photoUrl) {
        if (profileStore.containsKey(userId)) {
            throw new SportProfileAlreadyExistsException(
                "Player " + userId + " already has a sport profile");
        }
        SportProfile profile = new SportProfile();
        profile.setId(nextId++);
        profile.setPosition(position);
        profile.setJerseyNumber(jerseyNumber);
        profile.setPhotoUrl(photoUrl);
        profileStore.put(userId, profile);
        return profile;
    }

    public SportProfile updateProfile(Long userId, PlayingPosition position,
                                      Integer jerseyNumber, String photoUrl) {
        SportProfile profile = Optional.ofNullable(profileStore.get(userId))
            .orElseThrow(() -> new UserNotFoundException(
                "Sport profile not found for user: " + userId));

        if (!profile.canUpdate()) {
            throw new PlayerAlreadyAssignedToTeamException(
                "Cannot update sport profile: player is assigned to an active team");
        }
        if (position != null) profile.setPosition(position);
        if (jerseyNumber != null) profile.setJerseyNumber(jerseyNumber);
        profile.setPhotoUrl(photoUrl);
        return profile;
    }

    public void deleteProfile(Long userId) {
        throw new DeleteSportProfileNotAllowedException(
            "Deleting a sport profile is not allowed by business rules");
    }
}
