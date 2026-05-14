package edu.eci.userService.service;

import edu.eci.userService.exception.UserLinkedToActiveTournamentException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.model.User;
import edu.eci.userService.model.enums.SchoolRelation;
import edu.eci.userService.model.enums.UserStatus;
import edu.eci.userService.repository.UserDomainRepository;
import edu.eci.userService.services.UserDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserService - Unit Tests")
class UserServiceTest {

    @Mock private UserDomainRepository userRepository;
    @InjectMocks private UserDomainService userService;

    private User baseUser;

    @BeforeEach
    void setUp() {
        baseUser = new User();
        baseUser.setId(1L);
        baseUser.setFullName("Carlos Perez");
        baseUser.setEmail("carlos.perez@escuela.edu.co");
        baseUser.setSchoolRelation(SchoolRelation.STUDENT);
        baseUser.setAcademicProgram("Systems Engineering");
        baseUser.setSemester(5);
        baseUser.setStatus(UserStatus.ACTIVE);
        baseUser.setDateOfBirth(LocalDate.of(2000, 3, 15));
    }

    @Nested @DisplayName("Update User")
    class UpdateUserTests {

        @Test @DisplayName("Must update full name successfully")
        void mustUpdateFullNameSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
            User result = userService.updateUser(1L, "Juan Lopez", null, null, null);
            assertThat(result.getFullName()).isEqualTo("Juan Lopez");
            verify(userRepository).save(any(User.class));
        }

        @Test @DisplayName("Must update school relation successfully")
        void mustUpdateSchoolRelationSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
            User result = userService.updateUser(1L, null, SchoolRelation.GRADUATE, null, null);
            assertThat(result.getSchoolRelation()).isEqualTo(SchoolRelation.GRADUATE);
        }

        @Test @DisplayName("Must update academic program successfully")
        void mustUpdateAcademicProgramSuccessfully() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
            User result = userService.updateUser(1L, null, null, "AI Engineering", null);
            assertThat(result.getAcademicProgram()).isEqualTo("AI Engineering");
        }

        @Test @DisplayName("Must update semester when user is a student")
        void mustUpdateSemesterWhenUserIsStudent() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
            User result = userService.updateUser(1L, null, null, null, 7);
            assertThat(result.getSemester()).isEqualTo(7);
        }

        @Test @DisplayName("Must fail - must not allow updating the email address")
        void mustNotAllowUpdatingEmail() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            assertThatThrownBy(() -> userService.updateEmail(1L, "new@email.com"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("email");
        }

        @Test @DisplayName("Must fail - must not allow updating the password from this service")
        void mustNotAllowUpdatingPassword() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            assertThatThrownBy(() -> userService.updatePassword(1L, "newPassword123"))
                .isInstanceOf(UnsupportedOperationException.class)
                .hasMessageContaining("password");
        }

        @Test @DisplayName("Must fail - must throw exception when user does not exist")
        void mustThrowExceptionWhenUserDoesNotExistOnUpdate() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> userService.updateUser(99L, "Name", null, null, null))
                .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested @DisplayName("Deactivate User")
    class DeactivateUserTests {

        @Test @DisplayName("Must deactivate a user who is not linked to an active tournament")
        void mustDeactivateUserNotLinkedToActiveTournament() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            when(userRepository.isLinkedToActiveTournament(1L)).thenReturn(false);
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
            User result = userService.deactivateUser(1L);
            assertThat(result.getStatus()).isEqualTo(UserStatus.INACTIVE);
            verify(userRepository).save(any(User.class));
        }

        @Test @DisplayName("Must fail - must not deactivate user linked to an active tournament")
        void mustNotDeactivateUserLinkedToActiveTournament() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            when(userRepository.isLinkedToActiveTournament(1L)).thenReturn(true);
            assertThatThrownBy(() -> userService.deactivateUser(1L))
                .isInstanceOf(UserLinkedToActiveTournamentException.class)
                .hasMessageContaining("active tournament");
            verify(userRepository, never()).save(any());
        }

        @Test @DisplayName("Must fail - must not deactivate user linked to an in-progress tournament")
        void mustNotDeactivateUserLinkedToInProgressTournament() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            when(userRepository.isLinkedToActiveTournament(1L)).thenReturn(true);
            assertThatThrownBy(() -> userService.deactivateUser(1L))
                .isInstanceOf(UserLinkedToActiveTournamentException.class);
        }

        @Test @DisplayName("Must fail - must throw exception when user to deactivate does not exist")
        void mustThrowExceptionWhenUserDoesNotExistOnDeactivate() {
            when(userRepository.findById(55L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> userService.deactivateUser(55L))
                .isInstanceOf(UserNotFoundException.class);
        }

        @Test @DisplayName("Must set status to INACTIVE even when user is already inactive")
        void mustSetStatusToInactiveWhenAlreadyInactive() {
            baseUser.setStatus(UserStatus.INACTIVE);
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            when(userRepository.isLinkedToActiveTournament(1L)).thenReturn(false);
            when(userRepository.save(any(User.class))).thenAnswer(inv -> inv.getArgument(0));
            User result = userService.deactivateUser(1L);
            assertThat(result.getStatus()).isEqualTo(UserStatus.INACTIVE);
        }
    }

    @Nested @DisplayName("Query User")
    class QueryUserTests {

        @Test @DisplayName("Must return user by ID when it exists")
        void mustReturnUserByIdWhenExists() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(baseUser));
            User result = userService.findById(1L);
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(1L);
            assertThat(result.getFullName()).isEqualTo("Carlos Perez");
        }

        @Test @DisplayName("Must fail - must throw exception when ID does not exist")
        void mustThrowExceptionWhenIdDoesNotExist() {
            when(userRepository.findById(999L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> userService.findById(999L))
                .isInstanceOf(UserNotFoundException.class);
        }

        @Test @DisplayName("Must return user by email when it exists")
        void mustReturnUserByEmailWhenExists() {
            when(userRepository.findByEmail("carlos.perez@escuela.edu.co"))
                .thenReturn(Optional.of(baseUser));
            User result = userService.findByEmail("carlos.perez@escuela.edu.co");
            assertThat(result).isNotNull();
            assertThat(result.getEmail()).isEqualTo("carlos.perez@escuela.edu.co");
        }

        @Test @DisplayName("Must fail - must throw exception when email does not exist")
        void mustThrowExceptionWhenEmailDoesNotExist() {
            when(userRepository.findByEmail("unknown@escuela.edu.co")).thenReturn(Optional.empty());
            assertThatThrownBy(() -> userService.findByEmail("unknown@escuela.edu.co"))
                .isInstanceOf(UserNotFoundException.class);
        }
    }
}
