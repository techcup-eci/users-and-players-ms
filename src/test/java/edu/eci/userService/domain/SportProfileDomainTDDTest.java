package edu.eci.userService.domain;

import edu.eci.userService.model.SportProfile;
import edu.eci.userService.model.enums.PlayingPosition;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("SportProfile - Domain TDD Tests")
class SportProfileDomainTDDTest {

    @Nested @DisplayName("Default values on creation")
    class DefaultValuesTests {
        @Test @DisplayName("Must fail - a new sport profile must not be assigned to a team by default")
        void newProfileMustNotBeAssignedToTeamByDefault() {
            assertThat(new SportProfile().isAssignedToTeam()).isFalse();
        }
        @Test @DisplayName("Must fail - a new sport profile must have null photo URL by default")
        void newProfileMustHaveNullPhotoUrlByDefault() {
            assertThat(new SportProfile().getPhotoUrl()).isNull();
        }
    }

    @Nested @DisplayName("Jersey number validation")
    class JerseyNumberValidationTests {
        @Test @DisplayName("Must fail - must reject jersey number zero")
        void mustRejectJerseyNumberZero() {
            assertThatThrownBy(() -> new SportProfile().setJerseyNumber(0))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("jersey number");
        }
        @Test @DisplayName("Must fail - must reject negative jersey number")
        void mustRejectNegativeJerseyNumber() {
            assertThatThrownBy(() -> new SportProfile().setJerseyNumber(-1))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("jersey number");
        }
        @Test @DisplayName("Must fail - must reject jersey number greater than 99")
        void mustRejectJerseyNumberGreaterThan99() {
            assertThatThrownBy(() -> new SportProfile().setJerseyNumber(100))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("jersey number");
        }
        @ParameterizedTest @ValueSource(ints = {1, 9, 10, 50, 99})
        @DisplayName("Must accept valid jersey numbers between 1 and 99")
        void mustAcceptValidJerseyNumbers(int jerseyNumber) {
            SportProfile profile = new SportProfile();
            profile.setJerseyNumber(jerseyNumber);
            assertThat(profile.getJerseyNumber()).isEqualTo(jerseyNumber);
        }
    }

    @Nested @DisplayName("Playing position validation")
    class PlayingPositionValidationTests {
        @Test @DisplayName("Must fail - PlayingPosition enum must contain exactly four values")
        void playingPositionEnumMustContainExactlyFourValues() {
            assertThat(PlayingPosition.values()).containsExactlyInAnyOrder(
                PlayingPosition.GOALKEEPER, PlayingPosition.DEFENDER,
                PlayingPosition.MIDFIELDER, PlayingPosition.FORWARD);
        }
        @Test void mustAcceptGoalkeeperPosition() {
            SportProfile p = new SportProfile(); p.setPosition(PlayingPosition.GOALKEEPER);
            assertThat(p.getPosition()).isEqualTo(PlayingPosition.GOALKEEPER);
        }
        @Test void mustAcceptDefenderPosition() {
            SportProfile p = new SportProfile(); p.setPosition(PlayingPosition.DEFENDER);
            assertThat(p.getPosition()).isEqualTo(PlayingPosition.DEFENDER);
        }
        @Test void mustAcceptMidfielderPosition() {
            SportProfile p = new SportProfile(); p.setPosition(PlayingPosition.MIDFIELDER);
            assertThat(p.getPosition()).isEqualTo(PlayingPosition.MIDFIELDER);
        }
        @Test void mustAcceptForwardPosition() {
            SportProfile p = new SportProfile(); p.setPosition(PlayingPosition.FORWARD);
            assertThat(p.getPosition()).isEqualTo(PlayingPosition.FORWARD);
        }
        @Test @DisplayName("Must fail - must reject null playing position")
        void mustRejectNullPlayingPosition() {
            assertThatThrownBy(() -> new SportProfile().setPosition(null))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("position");
        }
    }

    @Nested @DisplayName("canUpdate business rule")
    class CanUpdateRuleTests {
        @Test void canUpdateMustReturnTrueWhenNotAssignedToTeam() {
            SportProfile p = new SportProfile(); p.setAssignedToTeam(false);
            assertThat(p.canUpdate()).isTrue();
        }
        @Test void canUpdateMustReturnFalseWhenAssignedToTeam() {
            SportProfile p = new SportProfile(); p.setAssignedToTeam(true);
            assertThat(p.canUpdate()).isFalse();
        }
        @Test void canUpdateMustReturnFalseAfterPlayerJoinsTeam() {
            SportProfile p = new SportProfile(); p.setAssignedToTeam(false);
            assertThat(p.canUpdate()).isTrue();
            p.setAssignedToTeam(true);
            assertThat(p.canUpdate()).isFalse();
        }
    }

    @Nested @DisplayName("canDelete business rule")
    class CanDeleteRuleTests {
        @Test void canDeleteMustAlwaysReturnFalseWhenNotAssigned() {
            SportProfile p = new SportProfile(); p.setAssignedToTeam(false);
            assertThat(p.canDelete()).isFalse();
        }
        @Test void canDeleteMustAlwaysReturnFalseWhenAssigned() {
            SportProfile p = new SportProfile(); p.setAssignedToTeam(true);
            assertThat(p.canDelete()).isFalse();
        }
    }

    @Nested @DisplayName("Photo URL validation")
    class PhotoUrlValidationTests {
        @Test void mustAcceptValidPhotoUrl() {
            SportProfile p = new SportProfile();
            p.setPhotoUrl("https://storage.techcup.com/photos/player_1.jpg");
            assertThat(p.getPhotoUrl()).isEqualTo("https://storage.techcup.com/photos/player_1.jpg");
        }
        @Test void mustAcceptNullPhotoUrlSincePhotoIsOptional() {
            SportProfile p = new SportProfile(); p.setPhotoUrl(null);
            assertThat(p.getPhotoUrl()).isNull();
        }
    }

    @Nested @DisplayName("Audit fields")
    class AuditFieldsTests {
        @Test void sportProfileEntityMustExposeCreatedAtField() {
            assertThatCode(new SportProfile()::getCreatedAt).doesNotThrowAnyException();
        }
        @Test void sportProfileEntityMustExposeUpdatedAtField() {
            assertThatCode(new SportProfile()::getUpdatedAt).doesNotThrowAnyException();
        }
    }
}
