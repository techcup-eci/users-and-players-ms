package edu.eci.userService.repository;

import edu.eci.userService.model.User;

import java.util.Optional;

/**
 * Domain-level repository interface for User.
 * Used by the domain services (UserService, JoinRequestService).
 * The JPA-based implementation delegates to UserJpaRepository internally.
 */
public interface UserDomainRepository {

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    User save(User user);

    boolean isLinkedToActiveTournament(Long userId);

    boolean existsById(Long id);
}
