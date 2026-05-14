package edu.eci.userService.repository;

import edu.eci.userService.model.JoinRequest;
import edu.eci.userService.model.enums.JoinRequestStatus;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for JoinRequest domain model.
 * Implementations may delegate to a JPA repository internally.
 */
public interface JoinRequestRepository {

    Optional<JoinRequest> findById(Long id);

    JoinRequest save(JoinRequest joinRequest);

    boolean existsByPlayerIdAndStatus(Long playerId, JoinRequestStatus status);

    List<JoinRequest> findByTeamIdAndStatus(Long teamId, JoinRequestStatus status);

    List<JoinRequest> findByPlayerId(Long playerId);
}
