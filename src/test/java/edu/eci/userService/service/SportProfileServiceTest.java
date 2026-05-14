package edu.eci.userService.service;

import edu.eci.userService.exception.DeleteSportProfileNotAllowedException;
import edu.eci.userService.exception.PlayerAlreadyAssignedToTeamException;
import edu.eci.userService.exception.SportProfileAlreadyExistsException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.model.SportProfile;
import edu.eci.userService.model.User;
import edu.eci.userService.model.enums.PlayingPosition;
import edu.eci.userService.model.enums.UserStatus;
import edu.eci.userService.services.SportProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Unit tests for SportProfileService.
 * Uses the in-memory store (no mocks needed for this service).
 */
@DisplayName("SportProfileService - Unit Tests")
class SportProfileServiceTest {

    private SportProfileService sportProfileService;
    private User player;

    @BeforeEach
    void setUp() {
        sportProfileService = new SportProfileService();
        player = new User();
        player.setId(1L);
        player.setFullName("Andres Torres");
        player.setStatus(UserStatus.ACTIVE);
    }

    @Nested @DisplayName("Create Sport Profile")
    class CreateSportProfileTests {

        @Test @DisplayName("Must create sport profile with valid data")
        void mustCreateSportProfileWithValidData() {
            SportProfile result = sportProfileService.createProfile(
                1L, PlayingPosition.GOALKEEPER, 1, "https://storage/photo.jpg");
            assertThat(result).isNotNull();
            assertThat(result.getPosition()).isEqualTo(PlayingPosition.GOALKEEPER);
            assertThat(result.getJerseyNumber()).isEqualTo(1);
        }

        @Test @DisplayName("Must create sport profile with DEFENDER position")
        void mustCreateSportProfileWithDefenderPosition() {
            SportProfile result = sportProfileService.createProfile(1L, PlayingPosition.DEFENDER, 4, null);
            assertThat(result.getPosition()).isEqualTo(PlayingPosition.DEFENDER);
        }

        @Test @DisplayName("Must create sport profile with MIDFIELDER position")
        void mustCreateSportProfileWithMidfielderPosition() {
            SportProfile result = sportProfileService.createProfile(1L, PlayingPosition.MIDFIELDER, 8, null);
            assertThat(result.getPosition()).isEqualTo(PlayingPosition.MIDFIELDER);
        }

        @Test @DisplayName("Must create sport profile without photo since photo is optional")
        void mustCreateSportProfileWithoutPhoto() {
            SportProfile result = sportProfileService.createProfile(1L, PlayingPosition.MIDFIELDER, 6, null);
            assertThat(result).isNotNull();
            assertThat(result.getPhotoUrl()).isNull();
        }

        @Test @DisplayName("Must fail - must not create a second profile when player already has one")
        void mustNotCreateSecondProfileWhenPlayerAlreadyHasOne() {
            sportProfileService.createProfile(1L, PlayingPosition.FORWARD, 9, null);
            assertThatThrownBy(() ->
                sportProfileService.createProfile(1L, PlayingPosition.FORWARD, 9, null)
            ).isInstanceOf(SportProfileAlreadyExistsException.class);
        }
    }

    @Nested @DisplayName("Update Sport Profile")
    class UpdateSportProfileTests {

        @BeforeEach
        void createProfile() {
            sportProfileService.createProfile(1L, PlayingPosition.FORWARD, 9,
                "https://storage/photo_andres.jpg");
        }

        @Test @DisplayName("Must update profile when player is not assigned to a team")
        void mustUpdateProfileWhenPlayerIsNotAssignedToTeam() {
            SportProfile result = sportProfileService.updateProfile(
                1L, PlayingPosition.GOALKEEPER, 1, "https://new-photo.jpg");
            assertThat(result.getPosition()).isEqualTo(PlayingPosition.GOALKEEPER);
            assertThat(result.getJerseyNumber()).isEqualTo(1);
            assertThat(result.getPhotoUrl()).isEqualTo("https://new-photo.jpg");
        }

        @Test @DisplayName("Must update only the photo without changing position or jersey number")
        void mustUpdateOnlyPhotoWithoutChangingOtherFields() {
            SportProfile result = sportProfileService.updateProfile(1L, null, null, "https://new-photo.jpg");
            assertThat(result.getPosition()).isEqualTo(PlayingPosition.FORWARD);
            assertThat(result.getJerseyNumber()).isEqualTo(9);
            assertThat(result.getPhotoUrl()).isEqualTo("https://new-photo.jpg");
        }

        @Test @DisplayName("Must fail - must not update profile when player is assigned to a team")
        void mustNotUpdateProfileWhenPlayerIsAssignedToTeam() {
            SportProfile profile = sportProfileService.getProfileByUser(1L);
            profile.setAssignedToTeam(true);
            assertThatThrownBy(() ->
                sportProfileService.updateProfile(1L, PlayingPosition.DEFENDER, 5, null)
            ).isInstanceOf(PlayerAlreadyAssignedToTeamException.class);
        }

        @Test @DisplayName("Must fail - must throw exception when profile does not exist on update")
        void mustThrowExceptionWhenProfileDoesNotExistOnUpdate() {
            assertThatThrownBy(() ->
                sportProfileService.updateProfile(99L, PlayingPosition.FORWARD, 10, null)
            ).isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested @DisplayName("Delete Sport Profile")
    class DeleteSportProfileTests {

        @Test @DisplayName("Must fail - must never allow deleting a sport profile")
        void mustNeverAllowDeletingSportProfile() {
            assertThatThrownBy(() -> sportProfileService.deleteProfile(1L))
                .isInstanceOf(DeleteSportProfileNotAllowedException.class)
                .hasMessageContaining("not allowed");
        }
    }

    @Nested @DisplayName("Query Sport Profile")
    class QuerySportProfileTests {

        @Test @DisplayName("Must return sport profile for an existing player")
        void mustReturnSportProfileForExistingPlayer() {
            sportProfileService.createProfile(1L, PlayingPosition.FORWARD, 9, null);
            SportProfile result = sportProfileService.getProfileByUser(1L);
            assertThat(result).isNotNull();
            assertThat(result.getPosition()).isEqualTo(PlayingPosition.FORWARD);
        }

        @Test @DisplayName("Must fail - must throw exception when player has no sport profile")
        void mustThrowExceptionWhenPlayerHasNoProfile() {
            assertThatThrownBy(() -> sportProfileService.getProfileByUser(1L))
                .isInstanceOf(UserNotFoundException.class);
        }
    }
}
