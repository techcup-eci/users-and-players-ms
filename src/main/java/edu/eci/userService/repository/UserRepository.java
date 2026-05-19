package edu.eci.userService.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import edu.eci.userService.entities.UserEntity;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    public List<UserEntity> findAll();

    public UserEntity findByEmail(String email);

    public UserEntity findUserByName(String name);

    public boolean existsById(Long id);

    public void deleteById(Long id);

    public List<UserEntity> findByRole(String role);

    public List<UserEntity> findByAcademicProgram(String academicProgram);

}
