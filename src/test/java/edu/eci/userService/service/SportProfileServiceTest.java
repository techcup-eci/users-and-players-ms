package com.techcup.users.service;

import com.techcup.users.exception.DeleteSportProfileNotAllowedException;
import com.techcup.users.exception.PlayerAlreadyAssignedToTeamException;
import com.techcup.users.exception.SportProfileAlreadyExistsException;
import com.techcup.users.exception.UserNotFoundException;
import com.techcup.users.model.SportProfile;
import com.techcup.users.model.User;
import com.techcup.users.model.enums.PlayingPosition;
import com.techcup.users.model.enums.UserStatus;
import com.techcup.users.repository.SportProfileRepository;
import com.techcup.users.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit tests for SportProfileService.
 *
 * Covers the functionalities defined in the TECHCUP FOOTBALL document (section 7.2):
 *   - Create sport profile
 *   - Update sport profile
 *   - Delete sport profile (forbidden by business rule)
 *   - Query sport profile
 *
 * Pattern: AAA (Arrange - Act - Assert)
 * Framework: JUnit 5 + Mockito
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("SportProfileService - Unit Tests")
class SportProfileServiceTest {

    @Mock
    private SportProfileRepository sportProfileRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SportProfileService sportProfileService;

    private User player;
    private SportProfile baseProfile;

    @BeforeEach
    void setUp() {
        player = new User();
        player.setId(1L);
        player.setFullName("Andres Torres");
        player.setStatus(UserStatus.ACTIVE);

        baseProfile = new SportProfile();
        baseProfile.setId(10L);
        baseProfile.setUser(player);
        baseProfile.setPosition(PlayingPosition.FORWARD);
        baseProfile.setJerseyNumber(9);
        baseProfile.setPhotoUrl("https://storage/photo_andres.jpg");
        baseProfile.setAssignedToTeam(false);
    }

    // ----------------------------------------------------------------
    // Create sport profile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Create Sport Profile")
    class CreateSportProfileTests {

        @Test
        @DisplayName("Must create sport profile with valid data")
        void mustCreateSportProfileWithValidData() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(sportProfileRepository.existsByUserId(1L)).thenReturn(false);
            when(sportProfileRepository.save(any(SportProfile.class))).thenAnswer(inv -> inv.getArgument(0));

            SportProfile result = sportProfileService.createProfile(
                1L, PlayingPosition.GOALKEEPER, 1, "https://storage/photo.jpg"
            );

            assertThat(result).isNotNull();
            assertThat(result.getPosition()).isEqualTo(PlayingPosition.GOALKEEPER);
            assertThat(result.getJerseyNumber()).isEqualTo(1);
            verify(sportProfileRepository).save(any(SportProfile.class));
        }

        @Test
        @DisplayName("Must create sport profile with DEFENDER position")
        void mustCreateSportProfileWithDefenderPosition() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(sportProfileRepository.existsByUserId(1L)).thenReturn(false);
            when(sportProfileRepository.save(any(SportProfile.class))).thenAnswer(inv -> inv.getArgument(0));

            SportProfile result = sportProfileService.createProfile(1L, PlayingPosition.DEFENDER, 4, null);

            assertThat(result.getPosition()).isEqualTo(PlayingPosition.DEFENDER);
        }

        @Test
        @DisplayName("Must create sport profile with MIDFIELDER position")
        void mustCreateSportProfileWithMidfielderPosition() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(sportProfileRepository.existsByUserId(1L)).thenReturn(false);
            when(sportProfileRepository.save(any(SportProfile.class))).thenAnswer(inv -> inv.getArgument(0));

            SportProfile result = sportProfileService.createProfile(1L, PlayingPosition.MIDFIELDER, 8, null);

            assertThat(result.getPosition()).isEqualTo(PlayingPosition.MIDFIELDER);
        }

        @Test
        @DisplayName("Must create sport profile without photo since photo is optional")
        void mustCreateSportProfileWithoutPhoto() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(sportProfileRepository.existsByUserId(1L)).thenReturn(false);
            when(sportProfileRepository.save(any(SportProfile.class))).thenAnswer(inv -> inv.getArgument(0));

            SportProfile result = sportProfileService.createProfile(1L, PlayingPosition.MIDFIELDER, 6, null);

            assertThat(result).isNotNull();
            assertThat(result.getPhotoUrl()).isNull();
        }

        @Test
        @DisplayName("Must fail - must not create a second profile when player already has one")
        void mustNotCreateSecondProfileWhenPlayerAlreadyHasOne() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(sportProfileRepository.existsByUserId(1L)).thenReturn(true);

            assertThatThrownBy(() ->
                sportProfileService.createProfile(1L, PlayingPosition.FORWARD, 9, null)
            ).isInstanceOf(SportProfileAlreadyExistsException.class)
             .hasMessageContaining("already has a profile");

            verify(sportProfileRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw exception when user does not exist on create")
        void mustThrowExceptionWhenUserDoesNotExistOnCreate() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                sportProfileService.createProfile(99L, PlayingPosition.FORWARD, 7, null)
            ).isInstanceOf(UserNotFoundException.class);
        }
    }

    // ----------------------------------------------------------------
    // Update sport profile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Update Sport Profile")
    class UpdateSportProfileTests {

        @Test
        @DisplayName("Must update profile when player is not assigned to a team")
        void mustUpdateProfileWhenPlayerIsNotAssignedToTeam() {
            baseProfile.setAssignedToTeam(false);
            when(sportProfileRepository.findByUserId(1L)).thenReturn(Optional.of(baseProfile));
            when(sportProfileRepository.save(any(SportProfile.class))).thenAnswer(inv -> inv.getArgument(0));

            SportProfile result = sportProfileService.updateProfile(
                1L, PlayingPosition.GOALKEEPER, 1, "https://new-photo.jpg"
            );

            assertThat(result.getPosition()).isEqualTo(PlayingPosition.GOALKEEPER);
            assertThat(result.getJerseyNumber()).isEqualTo(1);
            assertThat(result.getPhotoUrl()).isEqualTo("https://new-photo.jpg");
            verify(sportProfileRepository).save(any(SportProfile.class));
        }

        @Test
        @DisplayName("Must update only the photo without changing position or jersey number")
        void mustUpdateOnlyPhotoWithoutChangingOtherFields() {
            baseProfile.setAssignedToTeam(false);
            when(sportProfileRepository.findByUserId(1L)).thenReturn(Optional.of(baseProfile));
            when(sportProfileRepository.save(any(SportProfile.class))).thenAnswer(inv -> inv.getArgument(0));

            SportProfile result = sportProfileService.updateProfile(
                1L, null, null, "https://new-photo.jpg"
            );

            assertThat(result.getPosition()).isEqualTo(PlayingPosition.FORWARD);
            assertThat(result.getJerseyNumber()).isEqualTo(9);
            assertThat(result.getPhotoUrl()).isEqualTo("https://new-photo.jpg");
        }

        @Test
        @DisplayName("Must fail - must not update profile when player is assigned to a team")
        void mustNotUpdateProfileWhenPlayerIsAssignedToTeam() {
            baseProfile.setAssignedToTeam(true);
            when(sportProfileRepository.findByUserId(1L)).thenReturn(Optional.of(baseProfile));

            assertThatThrownBy(() ->
                sportProfileService.updateProfile(1L, PlayingPosition.DEFENDER, 5, null)
            ).isInstanceOf(PlayerAlreadyAssignedToTeamException.class)
             .hasMessageContaining("team");

            verify(sportProfileRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw exception when profile does not exist on update")
        void mustThrowExceptionWhenProfileDoesNotExistOnUpdate() {
            when(sportProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                sportProfileService.updateProfile(1L, PlayingPosition.FORWARD, 10, null)
            ).isInstanceOf(UserNotFoundException.class);
        }
    }

    // ----------------------------------------------------------------
    // Delete sport profile (forbidden)
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Delete Sport Profile")
    class DeleteSportProfileTests {

        @Test
        @DisplayName("Must fail - must never allow deleting a sport profile")
        void mustNeverAllowDeletingSportProfile() {
            assertThatThrownBy(() ->
                sportProfileService.deleteProfile(1L)
            ).isInstanceOf(DeleteSportProfileNotAllowedException.class)
             .hasMessageContaining("not allowed");

            verify(sportProfileRepository, never()).deleteById(any());
            verify(sportProfileRepository, never()).delete(any());
        }
    }

    // ----------------------------------------------------------------
    // Query sport profile
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Query Sport Profile")
    class QuerySportProfileTests {

        @Test
        @DisplayName("Must return sport profile for an existing player")
        void mustReturnSportProfileForExistingPlayer() {
            when(sportProfileRepository.findByUserId(1L)).thenReturn(Optional.of(baseProfile));

            SportProfile result = sportProfileService.getProfileByUser(1L);

            assertThat(result).isNotNull();
            assertThat(result.getPosition()).isEqualTo(PlayingPosition.FORWARD);
            assertThat(result.getJerseyNumber()).isEqualTo(9);
        }

        @Test
        @DisplayName("Must fail - must throw exception when player has no sport profile")
        void mustThrowExceptionWhenPlayerHasNoProfile() {
            when(sportProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

            assertThatThrownBy(() ->
                sportProfileService.getProfileByUser(1L)
            ).isInstanceOf(UserNotFoundException.class);
        }
    }
}
