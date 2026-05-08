package edu.eci.userService.domain;

import edu.eci.userService.model.User;
import edu.eci.userService.model.enums.SchoolRelation;
import edu.eci.userService.model.enums.UserStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/*
 * TDD tests for the User domain model.
 *
 * These tests define the expected behavior of the User class before
 * the implementation exists. Every test marked with "Must fail" means
 * the test will not pass until the corresponding logic is implemented.
 *
 * Tests without "Must fail" describe behavior that must be supported
 * from the first implementation of the class.
 *
 * Reference: TECHCUP FOOTBALL document - Section 7.2
 */
@DisplayName("User - Domain TDD Tests")
class UserDomainTDDTest {

    // ----------------------------------------------------------------
    // Default values on creation
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Default values on creation")
    class DefaultValuesTests {

        @Test
        @DisplayName("Must fail - a new user must have ACTIVE status by default")
        void newUserMustHaveActiveStatusByDefault() {
            User user = new User();

            assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);
        }

        @Test
        @DisplayName("Must fail - a new user must not be assigned to a team by default")
        void newUserMustNotBeAssignedToTeamByDefault() {
            User user = new User();

            assertThat(user.isAssignedToTeam()).isFalse();
        }
    }

    // ----------------------------------------------------------------
    // Field validations
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Field validations")
    class FieldValidationTests {

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("Must fail - must reject null or empty full name")
        void mustRejectNullOrEmptyFullName(String name) {
            User user = new User();

            assertThatThrownBy(() -> user.setFullName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("full name");
        }

        @Test
        @DisplayName("Must fail - must reject null email")
        void mustRejectNullEmail() {
            User user = new User();

            assertThatThrownBy(() -> user.setEmail(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("email");
        }

        @Test
        @DisplayName("Must fail - must reject a date of birth in the future")
        void mustRejectDateOfBirthInTheFuture() {
            User user = new User();

            assertThatThrownBy(() -> user.setDateOfBirth(LocalDate.now().plusYears(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("date of birth");
        }

        @Test
        @DisplayName("Must accept a valid institutional email for a student")
        void mustAcceptValidInstitutionalEmailForStudent() {
            User user = new User();
            user.setEmail("juan.gomez@escuela.edu.co");

            assertThat(user.getEmail()).isEqualTo("juan.gomez@escuela.edu.co");
        }

        @Test
        @DisplayName("Must accept a personal email for a family member")
        void mustAcceptPersonalEmailForFamilyMember() {
            User user = new User();
            user.setSchoolRelation(SchoolRelation.FAMILY);
            user.setEmail("relative@gmail.com");

            assertThat(user.getEmail()).isEqualTo("relative@gmail.com");
        }
    }

    // ----------------------------------------------------------------
    // School relation and semester
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("School relation and semester")
    class SchoolRelationTests {

        @Test
        @DisplayName("Must store student relation with semester")
        void mustStoreStudentRelationWithSemester() {
            User user = new User();
            user.setSchoolRelation(SchoolRelation.STUDENT);
            user.setSemester(3);

            assertThat(user.getSchoolRelation()).isEqualTo(SchoolRelation.STUDENT);
            assertThat(user.getSemester()).isEqualTo(3);
        }

        @Test
        @DisplayName("Must store graduate relation without semester")
        void mustStoreGraduateRelationWithoutSemester() {
            User user = new User();
            user.setSchoolRelation(SchoolRelation.GRADUATE);

            assertThat(user.getSchoolRelation()).isEqualTo(SchoolRelation.GRADUATE);
            assertThat(user.getSemester()).isNull();
        }
    }

    // ----------------------------------------------------------------
    // Deactivation business rule
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Deactivation business rule")
    class DeactivationRuleTests {

        @Test
        @DisplayName("Must fail - canDeactivate must return true when user is not linked to active tournament")
        void canDeactivateMustReturnTrueWhenNotLinkedToActiveTournament() {
            User user = new User();
            user.setStatus(UserStatus.ACTIVE);

            assertThat(user.canDeactivate(false)).isTrue();
        }

        @Test
        @DisplayName("Must fail - canDeactivate must return false when user is linked to active tournament")
        void canDeactivateMustReturnFalseWhenLinkedToActiveTournament() {
            User user = new User();
            user.setStatus(UserStatus.ACTIVE);

            assertThat(user.canDeactivate(true)).isFalse();
        }

        @Test
        @DisplayName("Must fail - canDeactivate must return false when user is linked to in-progress tournament")
        void canDeactivateMustReturnFalseWhenLinkedToInProgressTournament() {
            User user = new User();
            user.setStatus(UserStatus.ACTIVE);

            assertThat(user.canDeactivate(true)).isFalse();
        }
    }

    // ----------------------------------------------------------------
    // Join request business rule (maximum 1 pending)
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Join request business rule")
    class JoinRequestRuleTests {

        @Test
        @DisplayName("Must fail - canSendJoinRequest must return true when player has no pending requests")
        void canSendJoinRequestMustReturnTrueWhenNoPendingRequests() {
            User user = new User();

            assertThat(user.canSendJoinRequest(0)).isTrue();
        }

        @Test
        @DisplayName("Must fail - canSendJoinRequest must return false when player already has one pending request")
        void canSendJoinRequestMustReturnFalseWhenAlreadyHasPendingRequest() {
            User user = new User();

            assertThat(user.canSendJoinRequest(1)).isFalse();
        }
    }

    // ----------------------------------------------------------------
    // Audit fields
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Audit fields")
    class AuditFieldsTests {

        @Test
        @DisplayName("Must fail - User entity must expose createdAt field")
        void userEntityMustExposeCreatedAtField() {
            User user = new User();

            assertThatCode(user::getCreatedAt).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Must fail - User entity must expose updatedAt field")
        void userEntityMustExposeUpdatedAtField() {
            User user = new User();

            assertThatCode(user::getUpdatedAt).doesNotThrowAnyException();
        }
    }
}
