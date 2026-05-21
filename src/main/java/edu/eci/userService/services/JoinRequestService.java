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

    public JoinRequestDTO sendRequest(Long playerId, Long teamId) {
        UserEntity player = userRepository.findById(playerId)
            .orElseThrow(() -> new UserNotFoundException("Player not found with id: " + playerId));

        boolean hasPendingRequest = joinRequestRepository.existsByPlayerIdAndStatus(
            playerId, JoinRequestStatus.PENDING);
        if (hasPendingRequest) {
            throw new PendingJoinRequestException(
                "Player already has a pending request. Please wait or cancel the previous request.");
        }

        if (!isTeamAvailable(teamId)) {
            throw new TeamNotAvailableException("Team is not available with id: " + teamId);
        }

        JoinRequestEntity joinRequest = new JoinRequestEntity();
        joinRequest.setPlayer(player);
        joinRequest.setTeamId(teamId);
        joinRequest.setStatus(JoinRequestStatus.PENDING);

        JoinRequestEntity saved = joinRequestRepository.save(joinRequest);
        return joinRequestMapper.toDTO(saved);
    }

    public JoinRequestDTO acceptRequest(Long requestId, Long teamId) {
        JoinRequestEntity request = joinRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

        if (!request.getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("Request does not belong to team: " + teamId);
        }

        if (!request.canBeAccepted()) {
            throw new IllegalStateException("Request is not pending and cannot be accepted");
        }

        request.setStatus(JoinRequestStatus.ACCEPTED);
        JoinRequestEntity saved = joinRequestRepository.save(request);
        return joinRequestMapper.toDTO(saved);
    }

    public JoinRequestDTO rejectRequest(Long requestId, Long teamId) {
        JoinRequestEntity request = joinRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));

        if (!request.getTeamId().equals(teamId)) {
            throw new IllegalArgumentException("Request does not belong to team: " + teamId);
        }

        if (!request.canBeRejected()) {
            throw new IllegalStateException("Request is not pending and cannot be rejected");
        }

        request.setStatus(JoinRequestStatus.REJECTED);
        JoinRequestEntity saved = joinRequestRepository.save(request);
        return joinRequestMapper.toDTO(saved);
    }

    public JoinRequestDTO getRequestById(Long requestId) {
        JoinRequestEntity request = joinRequestRepository.findById(requestId)
            .orElseThrow(() -> new IllegalArgumentException("Request not found with id: " + requestId));
        return joinRequestMapper.toDTO(request);
    }

    public List<JoinRequestDTO> getRequestsByTeam(Long teamId) {
        List<JoinRequestEntity> requests = joinRequestRepository.findByTeamIdAndStatus(
            teamId, JoinRequestStatus.PENDING);
        return requests.stream()
            .map(joinRequestMapper::toDTO)
            .collect(Collectors.toList());
    }

    public List<JoinRequestDTO> getRequestsByPlayer(Long playerId) {
        List<JoinRequestEntity> requests = joinRequestRepository.findByPlayerId(playerId);
        return requests.stream()
            .map(joinRequestMapper::toDTO)
            .collect(Collectors.toList());
    }

    public boolean isTeamAvailable(Long teamId) {
        return teamId != null && teamId > 0;
    }
}
