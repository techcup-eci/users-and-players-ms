package edu.eci.userService.entities;

import jakarta.persistence.*;
import edu.eci.userService.enums.JoinRequestStatus;
import java.time.LocalDateTime;

@Entity
@Table(name = "join_requests")
public class JoinRequestEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id", nullable = false)
    private UserEntity player;

    @Column(nullable = false)
    private Long teamId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private JoinRequestStatus status;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    public JoinRequestEntity() {
        this.status = JoinRequestStatus.PENDING;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = JoinRequestStatus.PENDING;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Business logic methods
    public boolean isPending() {
        return this.status == JoinRequestStatus.PENDING;
    }

    public boolean canBeAccepted() {
        return this.status == JoinRequestStatus.PENDING;
    }

    public boolean canBeRejected() {
        return this.status == JoinRequestStatus.PENDING;
    }

    public boolean belongsToTeam(Long teamId) {
        if (teamId == null || this.teamId == null) {
            return false;
        }
        return this.teamId.equals(teamId);
    }

    public void accept() {
        if (!isPending()) {
            throw new IllegalStateException("Request is not pending and cannot be accepted");
        }
        this.status = JoinRequestStatus.ACCEPTED;
    }

    public void reject() {
        if (!isPending()) {
            throw new IllegalStateException("Request is not pending and cannot be rejected");
        }
        this.status = JoinRequestStatus.REJECTED;
    }

    // Validation
    public void setPlayer(UserEntity player) {
        if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }
        this.player = player;
    }

    public void setTeamId(Long teamId) {
        if (teamId == null || teamId < 0) {
            throw new IllegalArgumentException("team id cannot be null or negative");
        }
        this.teamId = teamId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getPlayer() {
        return player;
    }

    public Long getTeamId() {
        return teamId;
    }

    public JoinRequestStatus getStatus() {
        return status;
    }

    public void setStatus(JoinRequestStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
