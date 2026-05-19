package edu.eci.userService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.userService.entities.AthleticProfileEntity;

@Repository
public interface AthleticProfileRepository extends JpaRepository<AthleticProfileEntity, Long> {

    Optional<AthleticProfileEntity> findByUserId(Long userId);

    List<AthleticProfileEntity> findByPosition(String position);

    List<AthleticProfileEntity> findByLaterality(String laterality);

    boolean existsByUserId(Long userId);

    void deleteByUserId(Long userId);
}
