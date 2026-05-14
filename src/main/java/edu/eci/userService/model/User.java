package edu.eci.userService.model;

import edu.eci.userService.model.enums.SchoolRelation;
import edu.eci.userService.model.enums.UserStatus;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain model for User.
 * Contains business rules for the user lifecycle as defined in TECHCUP FOOTBALL document.
 */
public class User {

    private Long id;
    private String fullName;
    private String email;
    private LocalDate dateOfBirth;
    private SchoolRelation schoolRelation;
    private String academicProgram;
    private Integer semester;
    private UserStatus status = UserStatus.ACTIVE;
    private boolean assignedToTeam = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------

    public Long getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public SchoolRelation getSchoolRelation() { return schoolRelation; }
    public String getAcademicProgram() { return academicProgram; }
    public Integer getSemester() { return semester; }
    public UserStatus getStatus() { return status; }
    public boolean isAssignedToTeam() { return assignedToTeam; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ----------------------------------------------------------------
    // Setters with validation
    // ----------------------------------------------------------------

    public void setId(Long id) { this.id = id; }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("The full name must not be null or empty");
        }
        this.fullName = fullName;
    }

    public void setEmail(String email) {
        if (email == null) {
            throw new IllegalArgumentException("The email must not be null");
        }
        this.email = email;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        if (dateOfBirth != null && dateOfBirth.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("The date of birth cannot be in the future");
        }
        this.dateOfBirth = dateOfBirth;
    }

    public void setSchoolRelation(SchoolRelation schoolRelation) {
        this.schoolRelation = schoolRelation;
    }

    public void setAcademicProgram(String academicProgram) {
        this.academicProgram = academicProgram;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setAssignedToTeam(boolean assignedToTeam) {
        this.assignedToTeam = assignedToTeam;
    }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ----------------------------------------------------------------
    // Business rules
    // ----------------------------------------------------------------

    /**
     * A user can only be deactivated if they are not linked to an active tournament.
     */
    public boolean canDeactivate(boolean linkedToActiveTournament) {
        return !linkedToActiveTournament;
    }

    /**
     * A player can only send a new join request if they have no pending requests.
     */
    public boolean canSendJoinRequest(int pendingRequestCount) {
        return pendingRequestCount == 0;
    }
}
