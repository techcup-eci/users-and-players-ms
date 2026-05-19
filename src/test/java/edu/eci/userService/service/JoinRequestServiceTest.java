package edu.eci.userService.service;

import edu.eci.userService.dto.JoinRequestDTO;
import edu.eci.userService.dto.UserDTO;
import edu.eci.userService.entities.JoinRequestEntity;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.exception.PendingJoinRequestException;
import edu.eci.userService.exception.TeamNotAvailableException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.enums.JoinRequestStatus;
import edu.eci.userService.mappers.JoinRequestMapper;
import edu.eci.userService.repository.JoinRequestRepository;
import edu.eci.userService.repository.UserRepository;
import edu.eci.userService.services.JoinRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/*
 * Unit tests for JoinRequestService.
 *
 * Covers the full invitation system defined in the TECHCUP FOOTBALL document (section 7.2):
 *   - A player sends a join request to an available team
 *   - The system enforces a maximum of one pending request per player at a time
 *   - A captain accepts a pending request directed to their team
 *   - A captain rejects a pending request directed to their team
 *   - A captain cannot manage requests directed to other teams
 *   - A captain cannot act on a request that is no longer pending
 *   - A player cannot send a request to a team that is not available
 *   - List all pending requests for a given team
 *
 * Pattern: AAA (Arrange - Act - Assert)
 * Framework: JUnit 5 + Mockito
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("JoinRequestService - Unit Tests")
class JoinRequestServiceTest {

    @Mock
    private JoinRequestRepository joinRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JoinRequestMapper joinRequestMapper;

    private JoinRequestService joinRequestService;

    private UserEntity playerEntity;
    private UserDTO playerDTO;
    private JoinRequestEntity pendingEntity;
    private JoinRequestDTO pendingDTO;

    @BeforeEach
    void setUp() {
        joinRequestService = new JoinRequestService(joinRequestRepository, userRepository, joinRequestMapper);

        playerEntity = new UserEntity();
        playerEntity.setId(1L);
        playerEntity.setName("Luis Martinez");

        playerDTO = new UserDTO();
        playerDTO.setId(1L);
        playerDTO.setName("Luis Martinez");

        pendingEntity = new JoinRequestEntity();
        pendingEntity.setId(100L);
        pendingEntity.setPlayer(playerEntity);
        pendingEntity.setTeamId(5L);
        pendingEntity.setStatus(JoinRequestStatus.PENDING);

        pendingDTO = new JoinRequestDTO();
        pendingDTO.setId(100L);
        pendingDTO.setPlayer(playerDTO);
        pendingDTO.setTeamId(5L);
        pendingDTO.setStatus(JoinRequestStatus.PENDING);
    }

    // ----------------------------------------------------------------
    // Send join request
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Send join request")
    class SendJoinRequestTests {

        @Test
        @DisplayName("Must create a PENDING request when player has no existing pending request")
        void mustCreatePendingRequestWhenPlayerHasNoExistingPendingRequest() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(playerEntity));
            when(joinRequestRepository.existsByPlayerIdAndStatus(1L, JoinRequestStatus.PENDING))
                .thenReturn(false);
            when(joinRequestRepository.save(any(JoinRequestEntity.class))).thenReturn(pendingEntity);
            when(joinRequestMapper.toDTO(pendingEntity)).thenReturn(pendingDTO);

            JoinRequestDTO result = joinRequestService.sendRequest(1L, 5L);

            assertThat(result).isNotNull();
            assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.PENDING);
            assertThat(result.getTeamId()).isEqualTo(5L);
            verify(joinRequestRepository).save(any(JoinRequestEntity.class));
        }

        @Test
        @DisplayName("Must fail - must throw PendingJoinRequestException when player already has a pending request")
        void mustThrowPendingJoinRequestExceptionWhenPlayerAlreadyHasPendingRequest() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(playerEntity));
            when(joinRequestRepository.existsByPlayerIdAndStatus(1L, JoinRequestStatus.PENDING))
                .thenReturn(true);

            assertThatThrownBy(() -> joinRequestService.sendRequest(1L, 7L))
                .isInstanceOf(PendingJoinRequestException.class)
                .hasMessageContaining("pending request");

            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw UserNotFoundException when player does not exist")
        void mustThrowUserNotFoundExceptionWhenPlayerDoesNotExist() {
            when(userRepository.findById(99L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> joinRequestService.sendRequest(99L, 5L))
                .isInstanceOf(UserNotFoundException.class);

            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw TeamNotAvailableException when team ID is null or zero")
        void mustThrowTeamNotAvailableExceptionWhenTeamIdIsInvalid() {
            when(userRepository.findById(1L)).thenReturn(Optional.of(playerEntity));
            when(joinRequestRepository.existsByPlayerIdAndStatus(1L, JoinRequestStatus.PENDING))
                .thenReturn(false);

            assertThatThrownBy(() -> joinRequestService.sendRequest(1L, 0L))
                .isInstanceOf(TeamNotAvailableException.class);

            verify(joinRequestRepository, never()).save(any());
        }
    }

    // ----------------------------------------------------------------
    // Accept join request
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Accept join request")
    class AcceptJoinRequestTests {

        @Test
        @DisplayName("Must set status to ACCEPTED when request is pending and belongs to the captain team")
        void mustSetStatusToAcceptedWhenRequestIsPendingAndBelongsToCaptainTeam() {
            JoinRequestEntity savedEntity = new JoinRequestEntity();
            savedEntity.setId(100L);
            savedEntity.setPlayer(playerEntity);
            savedEntity.setTeamId(5L);
            savedEntity.setStatus(JoinRequestStatus.ACCEPTED);

            JoinRequestDTO acceptedDTO = new JoinRequestDTO();
            acceptedDTO.setId(100L);
            acceptedDTO.setStatus(JoinRequestStatus.ACCEPTED);

            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingEntity));
            when(joinRequestRepository.save(any(JoinRequestEntity.class))).thenReturn(savedEntity);
            when(joinRequestMapper.toDTO(savedEntity)).thenReturn(acceptedDTO);

            JoinRequestDTO result = joinRequestService.acceptRequest(100L, 5L);

            assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.ACCEPTED);
            verify(joinRequestRepository).save(any(JoinRequestEntity.class));
        }

        @Test
        @DisplayName("Must fail - must throw IllegalStateException when request is already accepted")
        void mustThrowIllegalStateExceptionWhenRequestIsAlreadyAccepted() {
            pendingEntity.setStatus(JoinRequestStatus.ACCEPTED);
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingEntity));

            assertThatThrownBy(() -> joinRequestService.acceptRequest(100L, 5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not pending");

            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw IllegalStateException when request is already rejected")
        void mustThrowIllegalStateExceptionWhenRequestIsAlreadyRejected() {
            pendingEntity.setStatus(JoinRequestStatus.REJECTED);
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingEntity));

            assertThatThrownBy(() -> joinRequestService.acceptRequest(100L, 5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not pending");

            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw IllegalArgumentException when request belongs to a different team")
        void mustThrowIllegalArgumentExceptionWhenRequestBelongsToDifferentTeam() {
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingEntity));

            assertThatThrownBy(() -> joinRequestService.acceptRequest(100L, 9L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("team");

            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw IllegalArgumentException when request does not exist")
        void mustThrowWhenRequestDoesNotExist() {
            when(joinRequestRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> joinRequestService.acceptRequest(999L, 5L))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ----------------------------------------------------------------
    // Reject join request
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Reject join request")
    class RejectJoinRequestTests {

        @Test
        @DisplayName("Must set status to REJECTED when request is pending and belongs to the captain team")
        void mustSetStatusToRejectedWhenRequestIsPendingAndBelongsToCaptainTeam() {
            JoinRequestEntity savedEntity = new JoinRequestEntity();
            savedEntity.setId(100L);
            savedEntity.setPlayer(playerEntity);
            savedEntity.setTeamId(5L);
            savedEntity.setStatus(JoinRequestStatus.REJECTED);

            JoinRequestDTO rejectedDTO = new JoinRequestDTO();
            rejectedDTO.setId(100L);
            rejectedDTO.setStatus(JoinRequestStatus.REJECTED);

            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingEntity));
            when(joinRequestRepository.save(any(JoinRequestEntity.class))).thenReturn(savedEntity);
            when(joinRequestMapper.toDTO(savedEntity)).thenReturn(rejectedDTO);

            JoinRequestDTO result = joinRequestService.rejectRequest(100L, 5L);

            assertThat(result.getStatus()).isEqualTo(JoinRequestStatus.REJECTED);
            verify(joinRequestRepository).save(any(JoinRequestEntity.class));
        }

        @Test
        @DisplayName("Must fail - must throw IllegalStateException when request is already rejected")
        void mustThrowIllegalStateExceptionWhenRequestIsAlreadyRejected() {
            pendingEntity.setStatus(JoinRequestStatus.REJECTED);
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingEntity));

            assertThatThrownBy(() -> joinRequestService.rejectRequest(100L, 5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not pending");

            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw IllegalStateException when request is already accepted")
        void mustThrowIllegalStateExceptionWhenRequestIsAlreadyAccepted() {
            pendingEntity.setStatus(JoinRequestStatus.ACCEPTED);
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingEntity));

            assertThatThrownBy(() -> joinRequestService.rejectRequest(100L, 5L))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("not pending");

            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw IllegalArgumentException when request belongs to a different team")
        void mustThrowIllegalArgumentExceptionWhenRequestBelongsToDifferentTeam() {
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingEntity));

            assertThatThrownBy(() -> joinRequestService.rejectRequest(100L, 8L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("team");

            verify(joinRequestRepository, never()).save(any());
        }

        @Test
        @DisplayName("Must fail - must throw IllegalArgumentException when request does not exist")
        void mustThrowWhenRequestDoesNotExist() {
            when(joinRequestRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> joinRequestService.rejectRequest(999L, 5L))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ----------------------------------------------------------------
    // List pending requests for a team
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("List pending requests")
    class ListPendingRequestsTests {

        @Test
        @DisplayName("Must return list with all pending requests for the given team")
        void mustReturnListWithAllPendingRequestsForGivenTeam() {
            when(joinRequestRepository.findByTeamIdAndStatus(5L, JoinRequestStatus.PENDING))
                .thenReturn(List.of(pendingEntity));
            when(joinRequestMapper.toDTO(pendingEntity)).thenReturn(pendingDTO);

            List<JoinRequestDTO> result = joinRequestService.getRequestsByTeam(5L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getTeamId()).isEqualTo(5L);
            assertThat(result.get(0).getStatus()).isEqualTo(JoinRequestStatus.PENDING);
        }

        @Test
        @DisplayName("Must return empty list when there are no pending requests for the team")
        void mustReturnEmptyListWhenNoPendingRequestsForTeam() {
            when(joinRequestRepository.findByTeamIdAndStatus(5L, JoinRequestStatus.PENDING))
                .thenReturn(List.of());

            List<JoinRequestDTO> result = joinRequestService.getRequestsByTeam(5L);

            assertThat(result).isEmpty();
        }
    }

    // ----------------------------------------------------------------
    // Find request by ID
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Find request by ID")
    class FindRequestByIdTests {

        @Test
        @DisplayName("Must return request when it exists")
        void mustReturnRequestWhenItExists() {
            when(joinRequestRepository.findById(100L)).thenReturn(Optional.of(pendingEntity));
            when(joinRequestMapper.toDTO(pendingEntity)).thenReturn(pendingDTO);

            JoinRequestDTO result = joinRequestService.getRequestById(100L);

            assertThat(result).isNotNull();
            assertThat(result.getId()).isEqualTo(100L);
        }

        @Test
        @DisplayName("Must fail - must throw IllegalArgumentException when request does not exist")
        void mustThrowWhenRequestDoesNotExist() {
            when(joinRequestRepository.findById(999L)).thenReturn(Optional.empty());

            assertThatThrownBy(() -> joinRequestService.getRequestById(999L))
                .isInstanceOf(IllegalArgumentException.class);
        }
    }

    // ----------------------------------------------------------------
    // Find requests by player
    // ----------------------------------------------------------------

    @Nested
    @DisplayName("Find requests by player")
    class FindRequestsByPlayerTests {

        @Test
        @DisplayName("Must return all requests sent by a given player")
        void mustReturnAllRequestsSentByGivenPlayer() {
            when(joinRequestRepository.findByPlayerId(1L)).thenReturn(List.of(pendingEntity));
            when(joinRequestMapper.toDTO(pendingEntity)).thenReturn(pendingDTO);

            List<JoinRequestDTO> result = joinRequestService.getRequestsByPlayer(1L);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).getPlayer().getId()).isEqualTo(1L);
        }

        @Test
        @DisplayName("Must return empty list when player has no requests")
        void mustReturnEmptyListWhenPlayerHasNoRequests() {
            when(joinRequestRepository.findByPlayerId(1L)).thenReturn(List.of());

            List<JoinRequestDTO> result = joinRequestService.getRequestsByPlayer(1L);

            assertThat(result).isEmpty();
        }

        @Test
        @DisplayName("Must return both pending and non-pending requests when querying by player")
        void mustReturnBothPendingAndNonPendingRequestsWhenQueryingByPlayer() {
            JoinRequestEntity rejectedEntity = new JoinRequestEntity();
            rejectedEntity.setId(101L);
            rejectedEntity.setPlayer(playerEntity);
            rejectedEntity.setTeamId(3L);
            rejectedEntity.setStatus(JoinRequestStatus.REJECTED);

            JoinRequestDTO rejectedDTO = new JoinRequestDTO();
            rejectedDTO.setId(101L);
            rejectedDTO.setPlayer(playerDTO);
            rejectedDTO.setTeamId(3L);
            rejectedDTO.setStatus(JoinRequestStatus.REJECTED);

            when(joinRequestRepository.findByPlayerId(1L))
                .thenReturn(List.of(pendingEntity, rejectedEntity));
            when(joinRequestMapper.toDTO(pendingEntity)).thenReturn(pendingDTO);
            when(joinRequestMapper.toDTO(rejectedEntity)).thenReturn(rejectedDTO);

            List<JoinRequestDTO> result = joinRequestService.getRequestsByPlayer(1L);

            assertThat(result).hasSize(2);
            assertThat(result).extracting(JoinRequestDTO::getStatus)
                .containsExactlyInAnyOrder(JoinRequestStatus.PENDING, JoinRequestStatus.REJECTED);
        }
    }
}
