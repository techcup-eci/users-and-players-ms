package edu.eci.userService.domain;

import edu.eci.userService.entities.UserEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
        @DisplayName("Must fail - a new user must have null fullName by default")
        void newUserMustHaveNullFullNameByDefault() {
            UserEntity user = new UserEntity();

            assertThat(user.getFullName()).isNull();
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
            UserEntity user = new UserEntity();

            assertThat(user.canSendJoinRequest(0)).isTrue();
        }

        @Test
        @DisplayName("Must fail - canSendJoinRequest must return false when player already has one pending request")
        void canSendJoinRequestMustReturnFalseWhenAlreadyHasPendingRequest() {
            UserEntity user = new UserEntity();

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
            UserEntity user = new UserEntity();

            assertThatCode(user::getCreatedAt).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("Must fail - User entity must expose updatedAt field")
        void userEntityMustExposeUpdatedAtField() {
            UserEntity user = new UserEntity();

            assertThatCode(user::getUpdatedAt).doesNotThrowAnyException();
        }
    }
}
