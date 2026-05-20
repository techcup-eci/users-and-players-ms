package edu.eci.userService.services;

import org.springframework.stereotype.Service;
import edu.eci.userService.dto.JoinRequestDTO;
import edu.eci.userService.entities.JoinRequestEntity;
import edu.eci.userService.entities.UserEntity;
import edu.eci.userService.enums.JoinRequestStatus;
import edu.eci.userService.exception.PendingJoinRequestException;
import edu.eci.userService.exception.TeamNotAvailableException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.mappers.JoinRequestMapper;
import edu.eci.userService.repository.JoinRequestRepository;
import edu.eci.userService.repository.UserRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JoinRequestService {

    private final JoinRequestRepository joinRequestRepository;
    private final UserRepository userRepository;
    private final JoinRequestMapper joinRequestMapper;

    public JoinRequestService(JoinRequestRepository joinRequestRepository,
                            UserRepository userRepository,
                            JoinRequestMapper joinRequestMapper) {
        this.joinRequestRepository = joinRequestRepository;
        this.userRepository = userRepository;
        this.joinRequestMapper = joinRequestMapper;
    }

    /**
     * Send a join request from a player to a team
     * 
     * @param playerId The ID of the player
     * @param teamId The ID of the team
     * @return The created JoinRequestDTO
     * @throws UserNotFoundException if player doesn't exist
     * @throws PendingJoinRequestException if player already has a pending request
     * @throws TeamNotAvailableException if team is not available
     */
    public JoinRequestDTO sendRequest(Long playerId, Long teamId) {
        // Verify player exists
        UserEntity player = userRepository.findById(playerId)
            .orElseThrow(() -> new UserNotFoundException("Player not found with id: " + playerId));

        // Check if player already has a pending request
        boolean hasPendingRequest = joinRequestRepository.existsByPlayerIdAndStatus(
            playerId, JoinRequestStatus.PENDING);
        if (hasPendingRequest) {
            throw new PendingJoinRequestException(
                "Player already has a pending request. Please wait or cancel the previous request.");
        }

        // Check if team is available (placeholder - adjust based on your team service)
        if (!isTeamAvailable(teamId)) {
            throw new TeamNotAvailableException("Team is not available with id: " + teamId);
        }

        // Create new join request
        JoinRequestEntity joinRequest = new JoinRequestEntity();
        joinRequest.setPlayer(player);
        joinRequest.setTeamId(teamId);
        joinRequest.setStatus(JoinRequestStatus.PENDING);

        JoinRequestEntity saved = joinRequestRepository.save(joinRequest);
        return joinRequestMapper.toDTO(saved);
    }

    /**
     * Accept a pending join request
     * 
     * @param requestId The ID of the join request
     * @param teamId The ID of the team (captain must be from this team)
     * @return The updated JoinRequestDTO
     */
    public JoinRequestDTO acceptRequest(Long requestId, Long teamId) {
        JoinRequestEntity request = joinRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

        // Verify request belongs to the team
        if (!request.getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("Request does not belong to team: " + teamId);
        }

        // Verify request is still pending
        if (!request.canBeAccepted()) {
            throw new IllegalStateException("Request is not pending and cannot be accepted");
        }

        request.setStatus(JoinRequestStatus.ACCEPTED);
        JoinRequestEntity saved = joinRequestRepository.save(request);
        return joinRequestMapper.toDTO(saved);
    }

    /**
     * Reject a pending join request
     * 
     * @param requestId The ID of the join request
     * @param teamId The ID of the team (captain must be from this team)
     * @return The updated JoinRequestDTO
     */
    public JoinRequestDTO rejectRequest(Long requestId, Long teamId) {
        JoinRequestEntity request = joinRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

        // Verify request belongs to the team
        if (!request.getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("Request does not belong to team: " + teamId);
        }

        // Verify request is still pending
        if (!request.canBeRejected()) {
            throw new IllegalStateException("Request is not pending and cannot be rejected");
        }

        request.setStatus(JoinRequestStatus.REJECTED);
        JoinRequestEntity saved = joinRequestRepository.save(request);
        return joinRequestMapper.toDTO(saved);
    }

    /**
     * Get a join request by ID
     * 
     * @param requestId The ID of the join request
     * @return The JoinRequestDTO
     */
    public JoinRequestDTO getRequestById(Long requestId) {
        JoinRequestEntity request = joinRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));
        return joinRequestMapper.toDTO(request);
    }

    /**
     * List all pending requests for a team
     * 
     * @param teamId The ID of the team
     * @return List of pending JoinRequestDTOs
     */
    public List<JoinRequestDTO> getRequestsByTeam(Long teamId) {
        List<JoinRequestEntity> requests = joinRequestRepository.findByTeamIdAndStatus(
            teamId, JoinRequestStatus.PENDING);
        return requests.stream()
            .map(joinRequestMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * List all requests for a player
     * 
     * @param playerId The ID of the player
     * @return List of JoinRequestDTOs
     */
    public List<JoinRequestDTO> getRequestsByPlayer(Long playerId) {
        List<JoinRequestEntity> requests = joinRequestRepository.findByPlayerId(playerId);
        return requests.stream()
            .map(joinRequestMapper::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Check if a team is available (placeholder - integrate with team service)
     * 
     * @param teamId The ID of the team
     * @return true if team is available
     */
    public boolean isTeamAvailable(Long teamId) {
        // TODO: Integrate with team microservice to verify team exists and is available
        // For now, we'll accept all team IDs > 0
        return teamId != null && teamId > 0;
    }
}
