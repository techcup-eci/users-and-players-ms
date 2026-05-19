package edu.eci.userService.dto;

import edu.eci.userService.enums.JoinRequestStatus;
import java.time.LocalDateTime;

public class JoinRequestDTO {

    private Long id;
    private UserDTO player;
    private Long teamId;
    private JoinRequestStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public JoinRequestDTO() {
    }

    public JoinRequestDTO(Long id, UserDTO player, Long teamId, JoinRequestStatus status,
                         LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.player = player;
        this.teamId = teamId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserDTO getPlayer() {
        return player;
    }

    public void setPlayer(UserDTO player) {
        this.player = player;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
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
