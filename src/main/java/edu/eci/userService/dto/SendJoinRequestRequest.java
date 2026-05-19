package edu.eci.userService.dto;

public class SendJoinRequestRequest {

    private Long teamId;

    public SendJoinRequestRequest() {
    }

    public SendJoinRequestRequest(Long teamId) {
        this.teamId = teamId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }
}
