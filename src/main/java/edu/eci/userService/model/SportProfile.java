package edu.eci.userService.model;

import edu.eci.userService.model.enums.PlayingPosition;

import java.time.LocalDateTime;

/**
 * Domain model for SportProfile.
 * Contains business rules for sport profile management as defined in TECHCUP FOOTBALL document.
 */
public class SportProfile {

    private Long id;
    private PlayingPosition position;
    private int jerseyNumber;
    private String photoUrl;
    private boolean assignedToTeam = false;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------

    public Long getId() { return id; }
    public PlayingPosition getPosition() { return position; }
    public int getJerseyNumber() { return jerseyNumber; }
    public String getPhotoUrl() { return photoUrl; }
    public boolean isAssignedToTeam() { return assignedToTeam; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ----------------------------------------------------------------
    // Setters with validation
    // ----------------------------------------------------------------

    public void setId(Long id) { this.id = id; }

    public void setPosition(PlayingPosition position) {
        if (position == null) {
            throw new IllegalArgumentException("The playing position must not be null");
        }
        this.position = position;
    }

    public void setJerseyNumber(int jerseyNumber) {
        if (jerseyNumber < 1 || jerseyNumber > 99) {
            throw new IllegalArgumentException("The jersey number must be between 1 and 99");
        }
        this.jerseyNumber = jerseyNumber;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
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
     * A sport profile can only be updated when the player is not assigned to a team.
     */
    public boolean canUpdate() {
        return !assignedToTeam;
    }

    /**
     * A sport profile can never be deleted.
     */
    public boolean canDelete() {
        return false;
    }
}
