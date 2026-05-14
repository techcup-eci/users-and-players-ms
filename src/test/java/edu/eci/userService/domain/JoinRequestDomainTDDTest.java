package edu.eci.userService.domain;

import edu.eci.userService.model.JoinRequest;
import edu.eci.userService.model.User;
import edu.eci.userService.model.enums.JoinRequestStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("JoinRequest - Domain TDD Tests")
class JoinRequestDomainTDDTest {

    private User player;
    private JoinRequest joinRequest;

    @BeforeEach
    void setUp() {
        player = new User();
        player.setId(1L);
        player.setFullName("Luis Martinez");

        joinRequest = new JoinRequest();
        joinRequest.setId(100L);
        joinRequest.setPlayer(player);
        joinRequest.setTeamId(5L);
        joinRequest.setStatus(JoinRequestStatus.PENDING);
    }

    @Nested @DisplayName("Default values on creation")
    class DefaultValuesTests {
        @Test void newJoinRequestMustHavePendingStatusByDefault() {
            assertThat(new JoinRequest().getStatus()).isEqualTo(JoinRequestStatus.PENDING);
        }
        @Test void newJoinRequestMustHaveNullPlayerByDefault() {
            assertThat(new JoinRequest().getPlayer()).isNull();
        }
        @Test void newJoinRequestMustHaveNullTeamIdByDefault() {
            assertThat(new JoinRequest().getTeamId()).isNull();
        }
    }

    @Nested @DisplayName("Field validations")
    class FieldValidationTests {
        @Test void mustRejectNullPlayer() {
            assertThatThrownBy(() -> new JoinRequest().setPlayer(null))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("player");
        }
        @Test void mustRejectNullTeamId() {
            assertThatThrownBy(() -> new JoinRequest().setTeamId(null))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("team");
        }
        @Test void mustRejectNegativeTeamId() {
            assertThatThrownBy(() -> new JoinRequest().setTeamId(-1L))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("team");
        }
        @Test void mustAcceptValidPlayerAndTeamId() {
            JoinRequest request = new JoinRequest();
            request.setPlayer(player);
            request.setTeamId(5L);
            assertThat(request.getPlayer()).isEqualTo(player);
            assertThat(request.getTeamId()).isEqualTo(5L);
        }
    }

    @Nested @DisplayName("isPending business rule")
    class IsPendingRuleTests {
        @Test void isPendingMustReturnTrueWhenStatusIsPending() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            assertThat(joinRequest.isPending()).isTrue();
        }
        @Test void isPendingMustReturnFalseWhenStatusIsAccepted() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);
            assertThat(joinRequest.isPending()).isFalse();
        }
        @Test void isPendingMustReturnFalseWhenStatusIsRejected() {
            joinRequest.setStatus(JoinRequestStatus.REJECTED);
            assertThat(joinRequest.isPending()).isFalse();
        }
    }

    @Nested @DisplayName("canBeAccepted business rule")
    class CanBeAcceptedRuleTests {
        @Test void canBeAcceptedMustReturnTrueWhenStatusIsPending() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            assertThat(joinRequest.canBeAccepted()).isTrue();
        }
        @Test void canBeAcceptedMustReturnFalseWhenAlreadyAccepted() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);
            assertThat(joinRequest.canBeAccepted()).isFalse();
        }
        @Test void canBeAcceptedMustReturnFalseWhenAlreadyRejected() {
            joinRequest.setStatus(JoinRequestStatus.REJECTED);
            assertThat(joinRequest.canBeAccepted()).isFalse();
        }
    }

    @Nested @DisplayName("canBeRejected business rule")
    class CanBeRejectedRuleTests {
        @Test void canBeRejectedMustReturnTrueWhenStatusIsPending() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            assertThat(joinRequest.canBeRejected()).isTrue();
        }
        @Test void canBeRejectedMustReturnFalseWhenAlreadyAccepted() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);
            assertThat(joinRequest.canBeRejected()).isFalse();
        }
        @Test void canBeRejectedMustReturnFalseWhenAlreadyRejected() {
            joinRequest.setStatus(JoinRequestStatus.REJECTED);
            assertThat(joinRequest.canBeRejected()).isFalse();
        }
    }

    @Nested @DisplayName("belongsToTeam business rule")
    class BelongsToTeamRuleTests {
        @Test void belongsToTeamMustReturnTrueWhenTeamIdMatches() {
            joinRequest.setTeamId(5L);
            assertThat(joinRequest.belongsToTeam(5L)).isTrue();
        }
        @Test void belongsToTeamMustReturnFalseWhenTeamIdDoesNotMatch() {
            joinRequest.setTeamId(5L);
            assertThat(joinRequest.belongsToTeam(9L)).isFalse();
        }
        @Test void belongsToTeamMustReturnFalseWhenProvidedTeamIdIsNull() {
            joinRequest.setTeamId(5L);
            assertThat(joinRequest.belongsToTeam(null)).isFalse();
        }
    }

    @Nested @DisplayName("Status transition rules")
    class StatusTransitionTests {
        @Test void statusMustChangeFromPendingToAccepted() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            joinRequest.accept();
            assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.ACCEPTED);
        }
        @Test void statusMustChangeFromPendingToRejected() {
            joinRequest.setStatus(JoinRequestStatus.PENDING);
            joinRequest.reject();
            assertThat(joinRequest.getStatus()).isEqualTo(JoinRequestStatus.REJECTED);
        }
        @Test void acceptMustThrowExceptionWhenStatusIsNotPending() {
            joinRequest.setStatus(JoinRequestStatus.REJECTED);
            assertThatThrownBy(joinRequest::accept)
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("not pending");
        }
        @Test void rejectMustThrowExceptionWhenStatusIsNotPending() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);
            assertThatThrownBy(joinRequest::reject)
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("not pending");
        }
        @Test void acceptMustThrowExceptionWhenAlreadyAccepted() {
            joinRequest.setStatus(JoinRequestStatus.ACCEPTED);
            assertThatThrownBy(joinRequest::accept).isInstanceOf(IllegalStateException.class);
        }
    }

    @Nested @DisplayName("JoinRequestStatus enum")
    class JoinRequestStatusEnumTests {
        @Test void joinRequestStatusEnumMustContainExactlyThreeValues() {
            assertThat(JoinRequestStatus.values()).containsExactlyInAnyOrder(
                JoinRequestStatus.PENDING, JoinRequestStatus.ACCEPTED, JoinRequestStatus.REJECTED);
        }
    }

    @Nested @DisplayName("Audit fields")
    class AuditFieldsTests {
        @Test void joinRequestEntityMustExposeCreatedAtField() {
            assertThatCode(new JoinRequest()::getCreatedAt).doesNotThrowAnyException();
        }
        @Test void joinRequestEntityMustExposeUpdatedAtField() {
            assertThatCode(new JoinRequest()::getUpdatedAt).doesNotThrowAnyException();
        }
    }
}
