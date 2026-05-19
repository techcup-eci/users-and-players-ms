package edu.eci.userService.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.eci.userService.entities.UserEntity;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    
    Optional<UserEntity> findById(Long id);
    
    Optional<UserEntity> findByEmail(String email);
    
    boolean existsByEmail(String email);
}
