package com.techcup.users.domain;

import com.techcup.users.model.SportProfile;
import com.techcup.users.model.enums.PlayingPosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*
 * TDD tests for the SportProfile domain model.
 *
 * These tests define the expected behavior of the SportProfile class
 * before the implementation exists. Every test marked with "Must fail"
 * means the test will not pass until the corresponding logic is implemented.
 *
 * Covered rules from TECHCUP FOOTBALL document (section 7.2):
 *   - Default values on creation
 *   - Jersey number validation (must be between 1 and 99)
 *   - Playing position validation (only 4 valid positions)
 *   - Business rule: profile cannot be updated when player is in a team
 *   - Business rule: profile can never be deleted
 *   - Audit fields presence
 *
 * Reference: TECHCUP FOOTBALL document - Section 7.2
 */
@DisplayName("SportProfile - Domain TDD Tests")
class SportProfileDomainTDDTest {

    // ----------------------------------------------------------------
    // Default values on creation
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Default values on creation")
    class DefaultValuesTests {

        @Test
        @DisplayName("Must fail - a new sport profile must not be assigned to a team by default")
        void newProfileMustNotBeAssignedToTeamByDefault() {
            SportProfile profile = new SportProfile();

            assertThat(profile.isAssignedToTeam()).isFalse();
        }

        @Test
        @DisplayName("Must fail - a new sport profile must have null photo URL by default")
        void newProfileMustHaveNullPhotoUrlByDefault() {
            SportProfile profile = new SportProfile();

            assertThat(profile.getPhotoUrl()).isNull();
        }
    }

    // ----------------------------------------------------------------
    // Jersey number validation
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Jersey number validation")
    class JerseyNumberValidationTests {

        @Test
        @DisplayName("Must fail - must reject jersey number zero")
        void mustRejectJerseyNumberZero() {
            SportProfile profile = new SportProfile();

            assertThatThrownBy(() -> profile.setJerseyNumber(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("jersey number");
        }

        @Test
        @DisplayName("Must fail - must reject negative jersey number")
        void mustRejectNegativeJerseyNumber() {
            SportProfile profile = new SportProfile();

            assertThatThrownBy(() -> profile.setJerseyNumber(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("jersey number");
        }

        @Test
        @DisplayName("Must fail - must reject jersey number greater than 99")
        void mustRejectJerseyNumberGreaterThan99() {
            SportProfile profile = new SportProfile();

            assertThatThrownBy(() -> profile.setJerseyNumber(100))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("jersey number");
        }

        @ParameterizedTest
        @ValueSource(ints = {1, 9, 10, 50, 99})
        @DisplayName("Must accept valid jersey numbers between 1 and 99")
        void mustAcceptValidJerseyNumbers(int jerseyNumber) {
            SportProfile profile = new SportProfile();
            profile.setJerseyNumber(jerseyNumber);

            assertThat(profile.getJerseyNumber()).isEqualTo(jerseyNumber);
        }
    }

    // ----------------------------------------------------------------
    // Playing position validation
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Playing position validation")
    class PlayingPositionValidationTests {

        @Test
        @DisplayName("Must fail - PlayingPosition enum must contain exactly four values")
        void playingPositionEnumMustContainExactlyFourValues() {
            assertThat(PlayingPosition.values()).containsExactlyInAnyOrder(
                PlayingPosition.GOALKEEPER,
                PlayingPosition.DEFENDER,
                PlayingPosition.MIDFIELDER,
                PlayingPosition.FORWARD
            );
        }

        @Test
        @DisplayName("Must accept GOALKEEPER as a valid playing position")
        void mustAcceptGoalkeeperPosition() {
            SportProfile profile = new SportProfile();
            profile.setPosition(PlayingPosition.GOALKEEPER);

            assertThat(profile.getPosition()).isEqualTo(PlayingPosition.GOALKEEPER);
        }

        @Test
        @DisplayName("Must accept DEFENDER as a valid playing position")
        void mustAcceptDefenderPosition() {
            SportProfile profile = new SportProfile();
            profile.setPosition(PlayingPosition.DEFENDER);

            assertThat(profile.getPosition()).isEqualTo(PlayingPosition.DEFENDER);
        }

        @Test
        @DisplayName("Must accept MIDFIELDER as a valid playing position")
        void mustAcceptMidfielderPosition() {
            SportProfile profile = new SportProfile();
            profile.setPosition(PlayingPosition.MIDFIELDER);

            assertThat(profile.getPosition()).isEqualTo(PlayingPosition.MIDFIELDER);
        }

        @Test
        @DisplayName("Must accept FORWARD as a valid playing position")
        void mustAcceptForwardPosition() {
            SportProfile profile = new SportProfile();
            profile.setPosition(PlayingPosition.FORWARD);

            assertThat(profile.getPosition()).isEqualTo(PlayingPosition.FORWARD);
        }

        @Test
        @DisplayName("Must fail - must reject null playing position")
        void mustRejectNullPlayingPosition() {
            SportProfile profile = new SportProfile();

            assertThatThrownBy(() -> profile.setPosition(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("position");
        }
    }

    // ----------------------------------------------------------------
    // canUpdate business rule
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("canUpdate business rule")
    class CanUpdateRuleTests {

        @Test
        @DisplayName("Must fail - canUpdate must return true when player is not assigned to a team")
        void canUpdateMustReturnTrueWhenNotAssignedToTeam() {
            SportProfile profile = new SportProfile();
            profile.setAssignedToTeam(false);

            assertThat(profile.canUpdate()).isTrue();
        }

        @Test
        @DisplayName("Must fail - canUpdate must return false when player is assigned to a team")
        void canUpdateMustReturnFalseWhenAssignedToTeam() {
            SportProfile profile = new SportProfile();
            profile.setAssignedToTeam(true);

            assertThat(profile.canUpdate()).isFalse();
        }

        @Test
        @DisplayName("Must fail - canUpdate must return false when player joins a team after profile creation")
        void canUpdateMustReturnFalseAfterPlayerJoinsTeam() {
            SportProfile profile = new SportProfile();
            profile.setAssignedToTeam(false);

            assertThat(profile.canUpdate()).isTrue();

            profile.setAssignedToTeam(true);

            assertThat(profile.canUpdate()).isFalse();
        }
    }

    // ----------------------------------------------------------------
    // canDelete business rule
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("canDelete business rule")
    class CanDeleteRuleTests {

        @Test
        @DisplayName("Must fail - canDelete must always return false regardless of assignment status")
        void canDeleteMustAlwaysReturnFalseWhenNotAssigned() {
            SportProfile profile = new SportProfile();
            profile.setAssignedToTeam(false);

            assertThat(profile.canDelete()).isFalse();
        }

        @Test
        @DisplayName("Must fail - canDelete must always return false even when assigned to a team")
        void canDeleteMustAlwaysReturnFalseWhenAssigned() {
            SportProfile profile = new SportProfile();
            profile.setAssignedToTeam(true);

            assertThat(profile.canDelete()).isFalse();
        }
    }

    // ----------------------------------------------------------------
    // Photo URL validation
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Photo URL validation")
    class PhotoUrlValidationTests {

        @Test
        @DisplayName("Must accept a valid photo URL")
        void mustAcceptValidPhotoUrl() {
            SportProfile profile = new SportProfile();
            profile.setPhotoUrl("https://storage.techcup.com/photos/player_1.jpg");

            assertThat(profile.getPhotoUrl()).isEqualTo("https://storage.techcup.com/photos/player_1.jpg");
        }

        @Test
        @DisplayName("Must accept null photo URL since photo is optional")
        void mustAcceptNullPhotoUrlSincePhotoIsOptional() {
            SportProfile profile = new SportProfile();
            profile.setPhotoUrl(null);

            assertThat(profile.getPhotoUrl()).isNull();
        }
    }

    // ----------------------------------------------------------------
    // Audit fields
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Audit fields")
    class AuditFieldsTests {

        @Test
        @DisplayName("Must fail - SportProfile entity must expose createdAt field")
        void sportProfileEntityMustExposeCreatedAtField() {
            SportProfile profile = new SportProfile();

            assertThatCode(profile::getCreatedAt).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Must fail - SportProfile entity must expose updatedAt field")
        void sportProfileEntityMustExposeUpdatedAtField() {
            SportProfile profile = new SportProfile();

            assertThatCode(profile::getUpdatedAt).doesNotThrowAnyException();
        }
    }
}
