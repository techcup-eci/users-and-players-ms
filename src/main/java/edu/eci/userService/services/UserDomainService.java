package edu.eci.userService.services;

import edu.eci.userService.exception.UserLinkedToActiveTournamentException;
import edu.eci.userService.exception.UserNotFoundException;
import edu.eci.userService.model.User;
import edu.eci.userService.model.enums.SchoolRelation;
import edu.eci.userService.model.enums.UserStatus;
import edu.eci.userService.repository.UserDomainRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Domain service for User management.
 * Implements business rules defined in TECHCUP FOOTBALL document section 7.2.
 */
@Service
public class UserDomainService {

    private final UserDomainRepository userRepository;

    public UserDomainService(UserDomainRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ----------------------------------------------------------------
    // Query
    // ----------------------------------------------------------------

    public User findById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
            .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    // ----------------------------------------------------------------
    // Update
    // ----------------------------------------------------------------

    public User updateUser(Long id, String fullName, SchoolRelation schoolRelation,
                           String academicProgram, Integer semester) {
        User user = findById(id);
        if (fullName != null) user.setFullName(fullName);
        if (schoolRelation != null) user.setSchoolRelation(schoolRelation);
        if (academicProgram != null) user.setAcademicProgram(academicProgram);
        if (semester != null) user.setSemester(semester);
        return userRepository.save(user);
    }

    public User updateEmail(Long id, String email) {
        throw new UnsupportedOperationException("Updating email is not allowed through this service");
    }

    public User updatePassword(Long id, String password) {
        throw new UnsupportedOperationException("Updating password is not allowed through this service");
    }

    // ----------------------------------------------------------------
    // Deactivation
    // ----------------------------------------------------------------

    public User deactivateUser(Long id) {
        User user = findById(id);
        boolean linkedToActiveTournament = userRepository.isLinkedToActiveTournament(id);
        if (!user.canDeactivate(linkedToActiveTournament)) {
            throw new UserLinkedToActiveTournamentException(
                "User " + id + " is linked to an active tournament and cannot be deactivated");
        }
        user.setStatus(UserStatus.INACTIVE);
        return userRepository.save(user);
    }
}
