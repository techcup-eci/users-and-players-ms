package edu.eci.userService.dto;

import jakarta.validation.constraints.NotNull;

/**
 * Request body for a player sending a join request to a team.
 */
public class SendJoinRequestRequest {

    @NotNull(message = "Team ID must not be null")
    private Long teamId;

    public Long getTeamId() { return teamId; }
    public void setTeamId(Long teamId) { this.teamId = teamId; }
}
