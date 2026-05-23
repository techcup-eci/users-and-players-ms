package edu.eci.userService.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.eci.userService.entities.AthleticProfileEntity;

@Repository
public interface AthleticProfileRepository extends JpaRepository<AthleticProfileEntity, Long> {

    public List<AthleticProfileEntity> findAll();

    public List<AthleticProfileEntity> findByPosition(String position);

    public List<AthleticProfileEntity> findByLaterality(String laterality);

    @Query("SELECT ap FROM AthleticProfileEntity ap JOIN ap.user u WHERE u.email = :email")
    Optional<AthleticProfileEntity> findByUserEmail(@Param("email") String email);
}
