package edu.eci.userService.repository;

import edu.eci.userService.entities.AthleticProfileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AthleticProfileRepository
        extends JpaRepository<AthleticProfileEntity, Long> {
}