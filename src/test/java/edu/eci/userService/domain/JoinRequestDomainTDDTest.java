package edu.eci.userService.domain;

import edu.eci.userService.entities.JoinRequestEntity;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.JoinRequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*
 * TDD tests for the JoinRequest domain model.
 *
 * These tests define the expected behavior of the JoinRequest class
 * before the implementation exists. Every test marked with "Must fail"
 * means the test will not pass until the corresponding logic is implemented.
 *
 * Covered rules from TECHCUP FOOTBALL document (section 7.2):
 *   - Default values on creation
 *   - Status transition rules (a request can only move forward, never backward)
 *   - Business rule: a player can only have one pending request at a time
 *   - Business rule: only a PENDING request can be accepted or rejected
 *   - Business rule: a captain can only manage requests directed to their team
 *   - Audit fields presence
 *
 * Reference: TECHCUP FOOTBALL document - Section 7.2
 */
@DisplayName("JoinRequest - Domain TDD Tests")
class JoinRequestDomainTDDTest {

    private UserEntity player;
    private JoinRequestEntity joinRequest;

    @BeforeEach
    void setUp() {
        player = new UserEntity();
        player.setId(1L);
        player.setName("Luis Martinez");

        joinRequest = new JoinRequestEntity();
        joinRequest.setId(100L);
        joinRequest.setPlayer(player);
        joinRequest.setTeamId(5L);
        joinRequest.setStatus(JoinRequestStatus.PENDING);
    }

    // ----------------------------------------------------------------
    // Default values on creation
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Default values on creation")
    class DefaultValuesTests {

        @Test
        @DisplayName("Must fail - a new join request must have PENDING status by default")
        void newJoinRequestMustHavePendingStatusByDefault() {
            JoinRequestEntity request = new JoinRequestEntity();

            assertThat(request.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
        }

        @Test
        @DisplayName("Must fail - a new join request must have null player by default")
        void newJoinRequestMustHaveNullPlayerByDefault() {
            JoinRequestEntity request = new JoinRequestEntity();

            assertThat(request.getPlayer()).isNull();
        }

        @Test
        @DisplayName("Must fail - a new join request must have null teamId by default")
        void newJoinRequestMustHaveNullTeamIdByDefault() {
            JoinRequestEntity request = new JoinRequestEntity();

            assertThat(request.getTeamId()).isNull();
        }
    }

    // ----------------------------------------------------------------
    // Field validations
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Field validations")
    class FieldValidationTests {

        @Test
        @DisplayName("Must fail - must reject null player when setting")
        void mustRejectNullPlayer() {
            JoinRequestEntity request = new JoinRequestEntity();

            assertThatThrownBy(() -> request.setPlayer(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("player");
        }

        @Test
        @DisplayName("Must fail - must reject null teamId when setting")
        void mustRejectNullTeamId() {
            JoinRequestEntity request = new JoinRequestEntity();

            assertThatThrownBy(() -> request.setTeamId(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("team");
        }

        @Test
        @DisplayName("Must fail - must reject negative teamId")
        void mustRejectNegativeTeamId() {
            JoinRequestEntity request = new JoinRequestEntity();

            assertThatThrownBy(() -> request.setTeamId(-1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("team");
        }

        @Test
        @DisplayName("Must accept valid player and teamId")
        void mustAcceptValidPlayerAndTeamId() {
            JoinRequestEntity request = new JoinRequestEntity();
            request.setPlayer(player);
            request.setTeamId(5L);

            assertThat(request.getPlayer()).isEqualTo(player);
            assertThat(request.getTeamId()).isEqualTo(5L);
        }
    }

    // ----------------------------------------------------------------
    // isPending business rule
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("isPending business rule")
    class IsPendingRuleTests {

        @Test
        @DisplayName("Must fail - isPending must return true when status is PENDING")
        void isPendingMustReturnTrueWhenStatusIsPending() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);

            assertThat(joinRequest.isPending()).isTrue();
        }

        @Test
        @DisplayName("Must fail - isPending must return false when status is ACCEPTED")
        void isPendingMustReturnFalseWhenStatusIsAccepted() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);

            assertThat(joinRequest.isPending()).isFalse();
        }

        @Test
        @DisplayName("Must fail - isPending must return false when status is REJECTED")
        void isPendingMustReturnFalseWhenStatusIsRejected() {
            joinRequest.setStatus(JoinRequestStatus.REJECTED);

            assertThat(joinRequest.isPending()).isFalse();
        }
    }

    // ----------------------------------------------------------------
    // canBeAccepted business rule
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("canBeAccepted business rule")
    class CanBeAcceptedRuleTests {

        @Test
        @DisplayName("Must fail - canBeAccepted must return true when status is PENDING")
        void canBeAcceptedMustReturnTrueWhenStatusIsPending() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);

            assertThat(joinRequest.canBeAccepted()).isTrue();
        }

        @Test
        @DisplayName("Must fail - canBeAccepted must return false when status is ACCEPTED")
        void canBeAcceptedMustReturnFalseWhenAlreadyAccepted() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);

            assertThat(joinRequest.canBeAccepted()).isFalse();
        }

        @Test
        @DisplayName("Must fail - canBeAccepted must return false when status is REJECTED")
        void canBeAcceptedMustReturnFalseWhenAlreadyRejected() {
            joinRequest.setStatus(JoinRequestStatus.REJECTED);

            assertThat(joinRequest.canBeAccepted()).isFalse();
        }
    }

    // ----------------------------------------------------------------
    // canBeRejected business rule
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("canBeRejected business rule")
    class CanBeRejectedRuleTests {

        @Test
        @DisplayName("Must fail - canBeRejected must return true when status is PENDING")
        void canBeRejectedMustReturnTrueWhenStatusIsPending() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);

            assertThat(joinRequest.canBeRejected()).isTrue();
        }

        @Test
        @DisplayName("Must fail - canBeRejected must return false when status is ACCEPTED")
        void canBeRejectedMustReturnFalseWhenAlreadyAccepted() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);

            assertThat(joinRequest.canBeRejected()).isFalse();
        }

        @Test
        @DisplayName("Must fail - canBeRejected must return false when status is REJECTED")
        void canBeRejectedMustReturnFalseWhenAlreadyRejected() {
            joinRequest.setStatus(JoinRequestStatus.REJECTED);

            assertThat(joinRequest.canBeRejected()).isFalse();
        }
    }

    // ----------------------------------------------------------------
    // belongsToTeam business rule
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("belongsToTeam business rule")
    class BelongsToTeamRuleTests {

        @Test
        @DisplayName("Must fail - belongsToTeam must return true when teamId matches")
        void belongsToTeamMustReturnTrueWhenTeamIdMatches() {
            joinRequest.setTeamId(5L);

            assertThat(joinRequest.belongsToTeam(5L)).isTrue();
        }

        @Test
        @DisplayName("Must fail - belongsToTeam must return false when teamId does not match")
        void belongsToTeamMustReturnFalseWhenTeamIdDoesNotMatch() {
            joinRequest.setTeamId(5L);

            assertThat(joinRequest.belongsToTeam(9L)).isFalse();
        }

        @Test
        @DisplayName("Must fail - belongsToTeam must return false when provided teamId is null")
        void belongsToTeamMustReturnFalseWhenProvidedTeamIdIsNull() {
            joinRequest.setTeamId(5L);

            assertThat(joinRequest.belongsToTeam(null)).isFalse();
        }
    }

    // ----------------------------------------------------------------
    // Status transition rules
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Status transition rules")
    class StatusTransitionTests {

        @Test
        @DisplayName("Must fail - status must change from PENDING to ACCEPTED")
        void statusMustChangeFromPendingToAccepted() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            joinRequest.accept();

            assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.ACCEPTED);
        }

        @Test
        @DisplayName("Must fail - status must change from PENDING to REJECTED")
        void statusMustChangeFromPendingToRejected() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            joinRequest.reject();

            assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.REJECTED);
        }

        @Test
        @DisplayName("Must fail - accept must throw exception when status is not PENDING")
        void acceptMustThrowExceptionWhenStatusIsNotPending() {
            joinRequest.setStatus(JoinRequestStatus.REJECTED);

            assertThatThrownBy(() -> joinRequest.accept())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not pending");
        }

        @Test
        @DisplayName("Must fail - reject must throw exception when status is not PENDING")
        void rejectMustThrowExceptionWhenStatusIsNotPending() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);

            assertThatThrownBy(() -> joinRequest.reject())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not pending");
        }

        @Test
        @DisplayName("Must fail - accept must throw exception when request is already accepted")
        void acceptMustThrowExceptionWhenAlreadyAccepted() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);

            assertThatThrownBy(() -> joinRequest.accept())
                .isInstanceOf(IllegalStateException.class);
        }
    }

    // ----------------------------------------------------------------
    // JoinRequestStatus enum
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("JoinRequestStatus enum")
    class JoinRequestStatusEnumTests {

        @Test
        @DisplayName("Must fail - JoinRequestStatus enum must contain exactly three values")
        void joinRequestStatusEnumMustContainExactlyThreeValues() {
            assertThat(JoinRequestStatus.values()).containsExactlyInAnyOrder(
                JoinRequestStatus.PENDING,
                JoinRequestStatus.ACCEPTED,
                JoinRequestStatus.REJECTED
            );
        }
    }

    // ----------------------------------------------------------------
    // Audit fields
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Audit fields")
    class AuditFieldsTests {

        @Test
        @DisplayName("Must fail - JoinRequest entity must expose createdAt field")
        void joinRequestEntityMustExposeCreatedAtField() {
            JoinRequestEntity request = new JoinRequestEntity();

            assertThatCode(request::getCreatedAt).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Must fail - JoinRequest entity must expose updatedAt field")
        void joinRequestEntityMustExposeUpdatedAtField() {
            JoinRequestEntity request = new JoinRequestEntity();

            assertThatCode(request::getUpdatedAt).doesNotThrowAnyException();
        }
    }
}
