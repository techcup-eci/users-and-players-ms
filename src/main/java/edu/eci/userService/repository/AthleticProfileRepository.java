package edu.eci.userService.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import edu.eci.userService.entities.AthleticProfileEntity;

@Repository
public interface AthleticProfileRepository extends JpaRepository<AthleticProfileEntity, Long> {

    public List<AthleticProfileEntity> findAll();

    public AthleticProfileEntity findByEmail(String email);

    public List<AthleticProfileEntity> findByPosition(String position);

    public List<AthleticProfileEntity> findByLaterality(String laterality);

    public boolean existsByEmail(String email);

    public void deleteByEmail(String email);
}
