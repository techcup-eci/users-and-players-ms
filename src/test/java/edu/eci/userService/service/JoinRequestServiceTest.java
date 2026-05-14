package edu.eci.userService.service;

import edu.eci.userService.exception.PendingJoinRequestException;
import edu.eci.userService.exception.TeamNotAvailableException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.model.JoinRequest;
import edu.eci.userService.model.User;
import edu.eci.userService.model.enums.JoinRequestStatus;
import edu.eci.userService.repository.JoinRequestRepository;
import edu.eci.userService.repository.UserDomainRepository;
import edu.eci.userService.services.JoinRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("JoinRequestService - Unit Tests")
class JoinRequestServiceTest {

    @Mock private JoinRequestRepository joinRequestRepository;
    @Mock private UserDomainRepository userRepository;
    @InjectMocks private JoinRequestService joinRequestService;

    private User player;
    private JoinRequest pendingRequest;

    @BeforeEach
    void setUp() {
        player = new User();
        player.setId(1L);
        player.setFullName("Luis Martinez");

        pendingRequest = new JoinRequest();
        pendingRequest.setId(100L);
        pendingRequest.setPlayer(player);
        pendingRequest.setTeamId(5L);
        pendingRequest.setStatus(JoinRequestStatus.PENDING);
    }

    @Nested @DisplayName("Send join request")
    class SendJoinRequestTests {

        @Test @DisplayName("Must create a PENDING request when player has no existing pending request")
        void mustCreatePendingRequestWhenPlayerHasNoExistingPendingRequest() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(joinRequestRepository.existsByPlayerIdAndStatus(1L, JoinRequestStatus.PENDING)).thenReturn(false);
            when(joinRequestRepository.save(any(JoinRequest.class))).thenAnswer(inv -> inv.getArgument(0));
            JoinRequest result = joinRequestService.sendRequest(1L, 5L);
            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
            assertThat(result.getTeamId()).isEqualTo(5L);
            verify(joinRequestRepository).save(any(JoinRequest.class));
        }

        @Test @DisplayName("Must persist the player reference inside the created request")
        void mustPersistPlayerReferenceInsideCreatedRequest() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(joinRequestRepository.existsByPlayerIdAndStatus(1L, JoinRequestStatus.PENDING)).thenReturn(false);
            when(joinRequestRepository.save(any(JoinRequest.class))).thenAnswer(inv -> inv.getArgument(0));
            JoinRequest result = joinRequestService.sendRequest(1L, 5L);
            assertThat(result.getPlayer()).isNotNull();
            assertThat(result.getPlayer().getId()).isEqualTo(1L);
        }

        @Test @DisplayName("Must fail - must throw PendingJoinRequestException when player already has a pending request")
        void mustThrowPendingJoinRequestExceptionWhenPlayerAlreadyHasPendingRequest() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(joinRequestRepository.existsByPlayerIdAndStatus(1L, JoinRequestStatus.PENDING)).thenReturn(true);
            assertThatThrownBy(() -> joinRequestService.sendRequest(1L, 7L))
                .isInstanceOf(PendingJoinRequestException.class)
                .hasMessageContaining("pending request");
            verify(joinRequestRepository, never()).save(any());
        }

        @Test @DisplayName("Must fail - must throw UserNotFoundException when player does not exist")
        void mustThrowUserNotFoundExceptionWhenPlayerDoesNotExist() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> joinRequestService.sendRequest(99L, 5L))
                .isInstanceOf(UserNotFoundException.class);
            verify(joinRequestRepository, never()).save(any());
        }

        @Test @DisplayName("Must allow sending a new request after a previous one was rejected")
        void mustAllowSendingNewRequestAfterPreviousOneWasRejected() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(joinRequestRepository.existsByPlayerIdAndStatus(1L, JoinRequestStatus.PENDING)).thenReturn(false);
            when(joinRequestRepository.save(any(JoinRequest.class))).thenAnswer(inv -> inv.getArgument(0));
            JoinRequest result = joinRequestService.sendRequest(1L, 8L);
            assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
        }

        @Test @DisplayName("Must allow sending a new request after a previous one was accepted")
        void mustAllowSendingNewRequestAfterPreviousOneWasAccepted() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(player));
            when(joinRequestRepository.existsByPlayerIdAndStatus(1L, JoinRequestStatus.PENDING)).thenReturn(false);
            when(joinRequestRepository.save(any(JoinRequest.class))).thenAnswer(inv -> inv.getArgument(0));
            JoinRequest result = joinRequestService.sendRequest(1L, 3L);
            assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
        }
    }

    @Nested @DisplayName("Accept join request")
    class AcceptJoinRequestTests {

        @Test @DisplayName("Must set status to ACCEPTED when request is pending and belongs to the captain team")
        void mustSetStatusToAcceptedWhenRequestIsPendingAndBelongsToCaptainTeam() {
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingRequest));
            when(joinRequestRepository.save(any(JoinRequest.class))).thenAnswer(inv -> inv.getArgument(0));
            JoinRequest result = joinRequestService.acceptRequest(100L, 5L);
            assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.ACCEPTED);
            verify(joinRequestRepository).save(any(JoinRequest.class));
        }

        @Test @DisplayName("Must fail - must throw IllegalStateException when request is already accepted")
        void mustThrowIllegalStateExceptionWhenRequestIsAlreadyAccepted() {
            pendingRequest.setStatus(JoinRequestStatus.ACCEPTED);
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingRequest));
            assertThatThrownBy(() -> joinRequestService.acceptRequest(100L, 5L))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("not pending");
            verify(joinRequestRepository, never()).save(any());
        }

        @Test @DisplayName("Must fail - must throw IllegalStateException when request is already rejected")
        void mustThrowIllegalStateExceptionWhenRequestIsAlreadyRejected() {
            pendingRequest.setStatus(JoinRequestStatus.REJECTED);
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingRequest));
            assertThatThrownBy(() -> joinRequestService.acceptRequest(100L, 5L))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("not pending");
            verify(joinRequestRepository, never()).save(any());
        }

        @Test @DisplayName("Must fail - must throw IllegalArgumentException when request belongs to a different team")
        void mustThrowIllegalArgumentExceptionWhenRequestBelongsToDifferentTeam() {
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingRequest));
            assertThatThrownBy(() -> joinRequestService.acceptRequest(100L, 9L))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("team");
            verify(joinRequestRepository, never()).save(any());
        }

        @Test @DisplayName("Must fail - must throw UserNotFoundException when request does not exist")
        void mustThrowUserNotFoundExceptionWhenRequestDoesNotExist() {
            when(joinRequestRepository.findById(999L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> joinRequestService.acceptRequest(999L, 5L))
                .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested @DisplayName("Reject join request")
    class RejectJoinRequestTests {

        @Test @DisplayName("Must set status to REJECTED when request is pending and belongs to the captain team")
        void mustSetStatusToRejectedWhenRequestIsPendingAndBelongsToCaptainTeam() {
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingRequest));
            when(joinRequestRepository.save(any(JoinRequest.class))).thenAnswer(inv -> inv.getArgument(0));
            JoinRequest result = joinRequestService.rejectRequest(100L, 5L);
            assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.REJECTED);
            verify(joinRequestRepository).save(any(JoinRequest.class));
        }

        @Test @DisplayName("Must fail - must throw IllegalStateException when request is already rejected")
        void mustThrowIllegalStateExceptionWhenRequestIsAlreadyRejected() {
            pendingRequest.setStatus(JoinRequestStatus.REJECTED);
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingRequest));
            assertThatThrownBy(() -> joinRequestService.rejectRequest(100L, 5L))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("not pending");
            verify(joinRequestRepository, never()).save(any());
        }

        @Test @DisplayName("Must fail - must throw IllegalStateException when request is already accepted")
        void mustThrowIllegalStateExceptionWhenRequestIsAlreadyAccepted() {
            pendingRequest.setStatus(JoinRequestStatus.ACCEPTED);
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingRequest));
            assertThatThrownBy(() -> joinRequestService.rejectRequest(100L, 5L))
                .isInstanceOf(IllegalStateException.class).hasMessageContaining("not pending");
            verify(joinRequestRepository, never()).save(any());
        }

        @Test @DisplayName("Must fail - must throw IllegalArgumentException when request belongs to a different team")
        void mustThrowIllegalArgumentExceptionWhenRequestBelongsToDifferentTeam() {
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingRequest));
            assertThatThrownBy(() -> joinRequestService.rejectRequest(100L, 8L))
                .isInstanceOf(IllegalArgumentException.class).hasMessageContaining("team");
            verify(joinRequestRepository, never()).save(any());
        }

        @Test @DisplayName("Must fail - must throw UserNotFoundException when request does not exist")
        void mustThrowUserNotFoundExceptionWhenRequestDoesNotExist() {
            when(joinRequestRepository.findById(999L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> joinRequestService.rejectRequest(999L, 5L))
                .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested @DisplayName("List pending requests")
    class ListPendingRequestsTests {

        @Test void mustReturnListWithAllPendingRequestsForGivenTeam() {
            when(joinRequestRepository.findByTeamIdAndStatus(5L, JoinRequestStatus.PENDING))
                .thenReturn(List.of(pendingRequest));
            List<JoinRequest> result = joinRequestService.listPendingRequests(5L);
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTeamId()).isEqualTo(5L);
        }

        @Test void mustReturnEmptyListWhenNoPendingRequestsForTeam() {
            when(joinRequestRepository.findByTeamIdAndStatus(5L, JoinRequestStatus.PENDING))
                .thenReturn(List.of());
            assertThat(joinRequestService.listPendingRequests(5L)).isEmpty();
        }

        @Test void mustReturnOnlyPendingRequestsAndNotOtherStatuses() {
            when(joinRequestRepository.findByTeamIdAndStatus(5L, JoinRequestStatus.PENDING))
                .thenReturn(List.of(pendingRequest));
            List<JoinRequest> result = joinRequestService.listPendingRequests(5L);
            assertThat(result).allMatch(r -> r.getStatus() == JoinRequestStatus.PENDING);
        }

        @Test void mustReturnMultiplePendingRequestsWhenMoreThanOnePlayerSentRequest() {
            JoinRequest secondRequest = new JoinRequest();
            secondRequest.setId(101L);
            secondRequest.setTeamId(5L);
            secondRequest.setStatus(JoinRequestStatus.PENDING);
            when(joinRequestRepository.findByTeamIdAndStatus(5L, JoinRequestStatus.PENDING))
                .thenReturn(List.of(pendingRequest, secondRequest));
            assertThat(joinRequestService.listPendingRequests(5L)).hasSize(2);
        }
    }

    @Nested @DisplayName("Find request by ID")
    class FindRequestByIdTests {

        @Test void mustReturnRequestWhenItExists() {
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingRequest));
            JoinRequest result = joinRequestService.findById(100L);
            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(100L);
        }

        @Test void mustThrowUserNotFoundExceptionWhenRequestDoesNotExist() {
            when(joinRequestRepository.findById(999L)).thenReturn(Optional.empty());
            assertThatThrownBy(() -> joinRequestService.findById(999L))
                .isInstanceOf(UserNotFoundException.class);
        }
    }

    @Nested @DisplayName("Find requests by player")
    class FindRequestsByPlayerTests {

        @Test void mustReturnAllRequestsSentByGivenPlayer() {
            when(joinRequestRepository.findByPlayerId(1L)).thenReturn(List.of(pendingRequest));
            List<JoinRequest> result = joinRequestService.findRequestsByPlayer(1L);
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPlayer().getId()).isEqualTo(1L);
        }

        @Test void mustReturnEmptyListWhenPlayerHasNoRequests() {
            when(joinRequestRepository.findByPlayerId(1L)).thenReturn(List.of());
            assertThat(joinRequestService.findRequestsByPlayer(1L)).isEmpty();
        }

        @Test void mustReturnBothPendingAndNonPendingRequestsWhenQueryingByPlayer() {
            JoinRequest rejectedRequest = new JoinRequest();
            rejectedRequest.setId(101L);
            rejectedRequest.setPlayer(player);
            rejectedRequest.setTeamId(3L);
            rejectedRequest.setStatus(JoinRequestStatus.REJECTED);
            when(joinRequestRepository.findByPlayerId(1L)).thenReturn(List.of(pendingRequest, rejectedRequest));
            List<JoinRequest> result = joinRequestService.findRequestsByPlayer(1L);
            assertThat(result).hasSize(2);
            assertThat(result).extracting(JoinRequest::getStatus)
                .containsExactlyInAnyOrder(JoinRequestStatus.PENDING, JoinRequestStatus.REJECTED);
        }
    }
}
