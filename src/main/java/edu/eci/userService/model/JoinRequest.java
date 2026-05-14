package edu.eci.userService.model;

import edu.eci.userService.model.enums.JoinRequestStatus;

import java.time.LocalDateTime;

/**
 * Domain model for JoinRequest.
 * Contains business rules for the join request lifecycle as defined in TECHCUP FOOTBALL document.
 */
public class JoinRequest {

    private Long id;
    private User player;
    private Long teamId;
    private JoinRequestStatus status = JoinRequestStatus.PENDING;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // ----------------------------------------------------------------
    // Getters
    // ----------------------------------------------------------------

    public Long getId() { return id; }
    public User getPlayer() { return player; }
    public Long getTeamId() { return teamId; }
    public JoinRequestStatus getStatus() { return status; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // ----------------------------------------------------------------
    // Setters with validation
    // ----------------------------------------------------------------

    public void setId(Long id) { this.id = id; }

    public void setPlayer(User player) {
        if (player == null) {
            throw new IllegalArgumentException("The player must not be null");
        }
        this.player = player;
    }

    public void setTeamId(Long teamId) {
        if (teamId == null) {
            throw new IllegalArgumentException("The team ID must not be null");
        }
        if (teamId < 0) {
            throw new IllegalArgumentException("The team ID must not be negative");
        }
        this.teamId = teamId;
    }

    public void setStatus(JoinRequestStatus status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // ----------------------------------------------------------------
    // Business rules
    // ----------------------------------------------------------------

    public boolean isPending() {
        return JoinRequestStatus.PENDING.equals(status);
    }

    public boolean canBeAccepted() {
        return isPending();
    }

    public boolean canBeRejected() {
        return isPending();
    }

    public boolean belongsToTeam(Long teamId) {
        if (teamId == null) return false;
        return teamId.equals(this.teamId);
    }

    /**
     * Transitions the status from PENDING to ACCEPTED.
     *
     * @throws IllegalStateException if the request is not pending
     */
    public void accept() {
        if (!isPending()) {
            throw new IllegalStateException("Cannot accept a request that is not pending");
        }
        this.status = JoinRequestStatus.ACCEPTED;
    }

    /**
     * Transitions the status from PENDING to REJECTED.
     *
     * @throws IllegalStateException if the request is not pending
     */
    public void reject() {
        if (!isPending()) {
            throw new IllegalStateException("Cannot reject a request that is not pending");
        }
        this.status = JoinRequestStatus.REJECTED;
    }
}
