package edu.eci.userService.services;

import edu.eci.userService.exception.PendingJoinRequestException;
import edu.eci.userService.exception.TeamNotAvailableException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.model.JoinRequest;
import edu.eci.userService.model.User;
import edu.eci.userService.model.enums.JoinRequestStatus;
import edu.eci.userService.repository.JoinRequestRepository;
import edu.eci.userService.repository.UserDomainRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for JoinRequest management.
 * Implements business rules defined in TECHCUP FOOTBALL document section 7.2.
 */
@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final UserDomainRepository userRepository;

    public JoinRequestService(JoinRequestRepository joinRequestRepository,
                               UserDomainRepository userRepository) {
        this.joinRequestRepository = joinRequestRepository;
        this.userRepository = userRepository;
    }

    // ----------------------------------------------------------------
    // Send request
    // ----------------------------------------------------------------

    public JoinRequest sendRequest(Long playerId, Long teamId) {
        User player = userRepository.findById(playerId)
            .orElseThrow(() -> new UserNotFoundException("Player not found: " + playerId));

        boolean hasPending = joinRequestRepository.existsByPlayerIdAndStatus(
            playerId, JoinRequestStatus.PENDING);
        if (hasPending) {
            throw new PendingJoinRequestException(
                "Player already has a pending request");
        }

        if (!isTeamAvailable(teamId)) {
            throw new TeamNotAvailableException("Team " + teamId + " is not available");
        }

        JoinRequest request = new JoinRequest();
        request.setPlayer(player);
        request.setTeamId(teamId);
        return joinRequestRepository.save(request);
    }

    // ----------------------------------------------------------------
    // Accept / Reject
    // ----------------------------------------------------------------

    public JoinRequest acceptRequest(Long requestId, Long captainTeamId) {
        JoinRequest request = findById(requestId);

        if (!request.belongsToTeam(captainTeamId)) {
            throw new IllegalArgumentException(
                "Request does not belong to team " + captainTeamId);
        }

        request.accept(); // throws IllegalStateException if not pending
        return joinRequestRepository.save(request);
    }

    public JoinRequest rejectRequest(Long requestId, Long captainTeamId) {
        JoinRequest request = findById(requestId);

        if (!request.belongsToTeam(captainTeamId)) {
            throw new IllegalArgumentException(
                "Request does not belong to team " + captainTeamId);
        }

        request.reject(); // throws IllegalStateException if not pending
        return joinRequestRepository.save(request);
    }

    // ----------------------------------------------------------------
    // Query
    // ----------------------------------------------------------------

    public JoinRequest findById(Long requestId) {
        return joinRequestRepository.findById(requestId)
            .orElseThrow(() -> new UserNotFoundException(
                "JoinRequest not found with ID: " + requestId));
    }

    public List<JoinRequest> listPendingRequests(Long teamId) {
        return joinRequestRepository.findByTeamIdAndStatus(teamId, JoinRequestStatus.PENDING);
    }

    public List<JoinRequest> findRequestsByPlayer(Long playerId) {
        return joinRequestRepository.findByPlayerId(playerId);
    }

    // ----------------------------------------------------------------
    // Availability check (can be overridden/mocked in tests)
    // ----------------------------------------------------------------

    public boolean isTeamAvailable(Long teamId) {
        // Default implementation — always true until external service is integrated
        return true;
    }
}
