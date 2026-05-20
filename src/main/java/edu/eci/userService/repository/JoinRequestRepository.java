package edu.eci.userService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.eci.userService.entities.JoinRequestEntity;
import edu.eci.userService.enums.JoinRequestStatus;
import java.util.List;
import java.util.Optional;

@Repository
public interface JoinRequestRepository extends JpaRepository<JoinRequestEntity, Long> {
    
    Optional<JoinRequestEntity> findById(Long id);
    
    List<JoinRequestEntity> findByTeamId(Long teamId);
    
    List<JoinRequestEntity> findByTeamIdAndStatus(Long teamId, JoinRequestStatus status);
    
    List<JoinRequestEntity> findByPlayerIdAndStatus(Long playerId, JoinRequestStatus status);
    
    List<JoinRequestEntity> findByPlayerId(Long playerId);
    
    boolean existsByPlayerIdAndStatus(Long playerId, JoinRequestStatus status);
}
